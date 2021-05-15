package com.example.pets.beforeAuth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.pets.R;

// THIS PROJECT BELONGS TO RAEHAT SINGH NANDA, DON'T COPY ANY PART OF IT WITHOUT PERMISSION
public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent intent= new Intent(Splash.this, intro.class);
                startActivity(intent);

                finish();
            }

        }, 1000);
    }
}