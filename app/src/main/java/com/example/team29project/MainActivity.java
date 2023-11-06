package com.example.team29project;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SlidingDrawer;
import android.widget.TextView;

import com.google.common.util.concurrent.ListenableFuture;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button camera ;
    TextView addItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton menu =findViewById(R.id.menu);
//        ConstraintLayout menuBackgroundLayout = (ConstraintLayout) findViewById(R.id.menu_background_layout);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMenu(view);
            }
        });

    }

    public void popupMenu(View view){
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.fragment_main_menu, null);
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, 750, height, focusable);
        popupWindow.showAtLocation(view, Gravity.LEFT, 0, 0);
        addItem = popupView.findViewById(R.id.add_new_item);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    new InputFragment().show(getSupportFragmentManager(), "addItems");
            }
        });


      popupView.setOnTouchListener((v, event) -> {
                    popupWindow.dismiss();
                    return true;
                });
    }
}