package com.example.team29project;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SlidingDrawer;

import com.google.common.util.concurrent.ListenableFuture;

public class MainActivity extends AppCompatActivity {
    Button camera ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton menu = (ImageButton)findViewById(R.id.menu);
        ConstraintLayout menuBackgroundLayout = (ConstraintLayout) findViewById(R.id.menu_background_layout);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstraintLayout menuLayout = (ConstraintLayout) findViewById(R.id.menu_layout);
                ConstraintLayout menuBackgroundLayout = (ConstraintLayout) findViewById(R.id.menu_background_layout);

                menuLayout.setTranslationX(400.f);
                menuBackgroundLayout.setTranslationX(400.f);
                menuBackgroundLayout.setBackgroundColor(getResources().getColor(R.color.tinted, null));
                menuBackgroundLayout.setElevation(2);
            }
        });

        menuBackgroundLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstraintLayout menuLayout = (ConstraintLayout) findViewById(R.id.menu_layout);
                ConstraintLayout menuBackgroundLayout = (ConstraintLayout) findViewById(R.id.menu_background_layout);

                menuLayout.setTranslationX(0.f);
                menuBackgroundLayout.setTranslationX(0.f);
                menuBackgroundLayout.setBackgroundColor(getResources().getColor(R.color.white, null));
                menuBackgroundLayout.setElevation(0);
            }
        });
    }
}