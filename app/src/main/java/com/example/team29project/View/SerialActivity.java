package com.example.team29project.View;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.team29project.R;
public class SerialActivity extends AppCompatActivity {

    private ImageView captureImageView;
    private TextView resultTextView;
    private Button snapButton, detectButton;
    private Bitmap serialBitmap;



    private final ActivityResultLauncher<Intent> captureActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Bundle extras = result.getData().getExtras();
                    serialBitmap = (Bitmap) extras.get("data");
                    captureImageView.setImageBitmap(serialBitmap);
                }
            }
    );

    private final ActivityResultLauncher<String> permissionActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    captureImage();
                }
            }
    );

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_serial);
        captureImageView = findViewById(R.id.serial_image_view);
        resultTextView = findViewById(R.id.serial_text_view);
        snapButton = findViewById(R.id.snap_button);
        detectButton = findViewById(R.id.detect_button);

        Toast.makeText(this, "SerialActivity started", Toast.LENGTH_SHORT).show();

        if (ContextCompat.checkSelfPermission(SerialActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionActivityResultLauncher.launch(android.Manifest.permission.CAMERA);
        } else {
            captureImage();
        }

        detectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detectText();
            }
        });

        snapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(SerialActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    permissionActivityResultLauncher.launch(android.Manifest.permission.CAMERA);
                } else {
                    captureImage();
                }
            }
        });
    }

    private void detectText() {
    }

    private void captureImage(){
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePicture.resolveActivity(getPackageManager())!=null){
            captureActivityResultLauncher.launch(takePicture);
        }
    }
}