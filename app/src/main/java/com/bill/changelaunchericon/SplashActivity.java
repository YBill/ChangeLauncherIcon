package com.bill.changelaunchericon;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

public class SplashActivity extends AppCompatActivity {

    private AppCompatTextView countDownTv;

    private CountDownTimer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        countDownTv = findViewById(R.id.tv_countdown);
        start();
    }

    private void start() {
        final int INTERVAL = 1000;
        mTimer = new CountDownTimer(5 * INTERVAL, INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                int time = (int) (millisUntilFinished / INTERVAL) + 1;
                countDownTv.setText(String.valueOf(time));
            }

            @Override
            public void onFinish() {
                stop();
                goMain();
            }
        };
        mTimer.start();
    }

    private void stop() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    private void goMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
