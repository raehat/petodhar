package com.example.pets.beforeAuth;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.pets.R;
import com.github.appintro.AppIntro;
import com.github.appintro.AppIntroFragment;

// THIS PROJECT BELONGS TO RAEHAT SINGH NANDA, DON'T COPY ANY PART OF IT WITHOUT PERMISSION
public class intro extends AppIntro {



    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

        @SuppressLint("WrongConstant") SharedPreferences sh
                = getSharedPreferences("MySharedPref", MODE_APPEND);
        int tf = sh.getInt("tf", 0);

        if (tf==1)
        {
            startActivity(new Intent(getApplicationContext(), loginOrRegister.class));
        }


        new AlertDialog.Builder(intro.this)
                .setTitle("Copyright©2020- Raehat Singh Nanda")
                .setMessage("All rights reserved. This app was developed by " +
                        "Developer R.S. Nanda so No part of this publication may be reproduced, distributed," +
                        " or transmitted in any form or by any means, including photocopying, recording, " +
                        "or other electronic or mechanical methods, without the prior written permission of " +
                        "the publisher, except in the case of" +
                        " brief quotations embodied in critical reviews and certain " +
                        "other noncommercial uses permitted by copyright law.")
                .setNeutralButton("Ok, I understand", null).show();


        addSlide(AppIntroFragment.newInstance(
                "Welcome...",
                "We are really thankful for you to download this app. You can either skip this intro or bother" +
                        " yourself for this lol!"
        ));
        addSlide(AppIntroFragment.newInstance(
                "...Let's get started!",
                "Very nycccc great success!!!", R.drawable.bg3
        ));
        addSlide(AppIntroFragment.newInstance(
                "Last slide lmaoo",
                "You're all done!! Now you can either login or" +
                        " create an account if you're new ehehe)"
        ));
    }

    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        Intent intent= new Intent(intro.this, loginOrRegister.class);
        startActivity(intent);
        // Decide what to do when the user clicks on "Skip"
        finish();
    }

    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Decide what to do when the user clicks on "Done"
        Intent intent= new Intent(intro.this, loginOrRegister.class);
        startActivity(intent);
        finish();
    }

}

