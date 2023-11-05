package com.example.team29project;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class DisplayImageActivity extends AppCompatActivity {
    private ImageView images;
    private Button camera_btn;
    private Button gallery_btn;
    ActivityResultLauncher<Intent> pictureActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        images.setImageURI(data.getData());
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagedisplay);
        images = findViewById(R.id.image_pic);
        camera_btn = findViewById(R.id.takePhoto_btn);
        gallery_btn= findViewById((R.id.pickPhoto_btn));
        gallery_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery_intent = new Intent(Intent.ACTION_PICK);
                gallery_intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                gallery_intent.setAction(Intent.ACTION_PICK);
                pictureActivityResultLauncher.launch(gallery_intent);

            }
        });
        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent customCamera = new Intent(DisplayImageActivity.this, CustomCameraActivity.class);
                pictureActivityResultLauncher.launch(customCamera);
            }
        });

    }
}