package com.example.team29project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/**
 * Display details of a selected inventory item
 * Allow users to view and potentially edit the item's information
 */
public class DisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
    }
}