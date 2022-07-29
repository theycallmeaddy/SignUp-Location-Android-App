package com.example.loginregistration.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.loginregistration.R;

public class SplashActivity extends AppCompatActivity {

    Handler h = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        h.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i = new Intent(SplashActivity.this, SignInActivity.class);
                startActivity(i);
                finish();
            }
        },3000);

    }
}