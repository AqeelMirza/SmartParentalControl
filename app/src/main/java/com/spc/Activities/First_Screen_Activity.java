package com.spc.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.spc.R;


public class First_Screen_Activity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.first_screen);

        Button parent_btn = (Button) findViewById(R.id.parent_btn);
        Button child_btn = (Button) findViewById(R.id.child_btn);

        parent_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(First_Screen_Activity.this, Login.class);
                startActivity(i);
            }
        });
        child_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(First_Screen_Activity.this, Get_Child_Activity.class);
                startActivity(i);
            }
        });

    }
}
