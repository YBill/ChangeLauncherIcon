package com.bill.changelaunchericon;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String FESTIVAL_NAME = ".festival.SplashActivity_festival";
    private static final String DEFAULT_NAME = ".festival.SplashActivity_normal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void handleChange(View view) {

        if (isFestival()) {
            toast("already change");
            return;
        }

        changeLauncher(FESTIVAL_NAME, DEFAULT_NAME);
        toast("change");

        // 重启 Launcher，为了使新icon赶快生效，不过好像并没什么卵用
        restartSystemLauncher(getPackageManager());

    }

    public void handleReset(View view) {

        if (!isFestival()) {
            toast("already reset");
            return;
        }

        changeLauncher(DEFAULT_NAME, FESTIVAL_NAME);
        toast("reset");
    }

    // 切换后大约10秒左右才生效，在此期间点击桌面icon会提示"未安装该应用"
    private void changeLauncher(String activeName, String disableName) {
        String pkgName = getPackageName();

        getPackageManager().setComponentEnabledSetting(new ComponentName(pkgName, pkgName + activeName),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        getPackageManager().setComponentEnabledSetting(new ComponentName(pkgName, pkgName + disableName),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    private void restartSystemLauncher(PackageManager pm) {
        ActivityManager am = (ActivityManager) getSystemService(Activity.ACTIVITY_SERVICE);
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        List<ResolveInfo> resolves = pm.queryIntentActivities(i, 0);
        for (ResolveInfo res : resolves) {
            if (res.activityInfo != null) {
                am.killBackgroundProcesses(res.activityInfo.packageName);
            }
        }
    }

    private boolean isFestival() {
        return getPackageManager().getComponentEnabledSetting(
                new ComponentName(getPackageName(), getPackageName() + FESTIVAL_NAME))
                == PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
    }

    private void toast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
