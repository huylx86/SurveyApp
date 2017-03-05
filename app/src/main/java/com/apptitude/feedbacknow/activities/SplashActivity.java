package com.apptitude.feedbacknow.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.apptitude.feedbacknow.R;


public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mHandlerClose.sendEmptyMessageDelayed(0, 2000);
    }

    private Handler mHandlerClose = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
            super.handleMessage(msg);
        }
    };
}
