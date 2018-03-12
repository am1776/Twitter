package com.amallya.twittermvvm;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.amallya.twittermvvm.login.LoginActivity;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DURATION = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent newActivityIntent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(newActivityIntent);
                finish();
            }
        }, SPLASH_DURATION);
    }
}
