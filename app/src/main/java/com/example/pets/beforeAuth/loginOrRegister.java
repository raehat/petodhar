package com.example.pets.beforeAuth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pets.R;
import com.example.pets.screen;

public class loginOrRegister extends AppCompatActivity {

    TextView textView3;
    TextView textView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_register);

        getSupportActionBar().hide();
        SharedPreferences sharedPreferences
                = getSharedPreferences("MySharedPref",
                MODE_PRIVATE);
        SharedPreferences.Editor myEdit
                = sharedPreferences.edit();
        myEdit.putInt("tf",
                Integer.parseInt(
                        String.valueOf(1)));
        myEdit.commit();

        if (SaveSharedPreference.getLoggedStatus(getApplicationContext()))
        {
            Intent intent= new Intent(getApplicationContext(), screen.class);
            startActivity(intent);
        }

        if (SaveSharedPreference.getLoggedStatus(getApplicationContext()))
        {
            finish();
            System.exit(0);
        }

        Button login= (Button) findViewById(R.id.login);
        Button account= (Button) findViewById(R.id.account);
        textView3= (TextView) findViewById(R.id.textView3);
        textView4= (TextView) findViewById(R.id.textView4);

        Animation animation= AnimationUtils.loadAnimation(this, R.anim.text_view_anim);
        Animation animationButton= AnimationUtils.loadAnimation(this, R.anim.button_view_anim);
        textView3.startAnimation(animation);
        textView4.startAnimation(animation);
        login.startAnimation(animationButton);
        account.startAnimation(animationButton);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), com.example.pets.beforeAuth.login.class));
            }
        });

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),   GetLocation.class));
            }
        });
    }
}