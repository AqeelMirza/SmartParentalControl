package com.spc.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.spc.R;


public class SplashScreen_Activity extends AppCompatActivity {

    private int splashTime = 2000; //2secs
    ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        //Applying the rotation Animation to logo
        imageView = findViewById(R.id.splashscreen_img);
        Animation rotation = AnimationUtils.loadAnimation(SplashScreen_Activity.this, R.anim.rotate);
        rotation.setFillAfter(true);
        imageView.startAnimation(rotation);
//delaying the loading of  First Activity by 2secs
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                goToFirstScreen();
            }
        }, splashTime);
    }

    //Navigate to FirstScreen
    private void goToFirstScreen() {

        Intent main_intent = new Intent(SplashScreen_Activity.this, First_Screen_Activity.class);
        startActivity(main_intent);
        finish();

    }
}
