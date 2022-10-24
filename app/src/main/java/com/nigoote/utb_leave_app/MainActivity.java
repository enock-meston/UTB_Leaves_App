package com.nigoote.utb_leave_app;

import static com.nigoote.utb_leave_app.LoginActivity.SHARED_PREFERENCES_NAME;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    public static int SPLASH_TIME_OUT = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent homeIntent= new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(homeIntent);
                finish();
            }
        },SPLASH_TIME_OUT);

    }
}