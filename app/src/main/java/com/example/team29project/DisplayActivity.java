package com.example.team29project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

public class DisplayActivity extends AppCompatActivity {
    private ImageView images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        images = findViewById(R.id.image_pic);
        Intent display_image = getIntent();
        Uri myUri = Uri.parse( display_image.getStringExtra("imageUri"));
        try {
            images.setImageURI(myUri);
        }catch (Exception ex) {
            Toast.makeText(this, "nah", Toast.LENGTH_SHORT).show();
        }


    }
}