package com.example.team29project;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;

public class CustomCameraActivity extends AppCompatActivity {
    private CustomCamera customCamera;
    private PreviewView previewView;
    private ImageView capturedImage;
    private Button okButton;
    private Button reTryButton;

    private static final int REQUEST_CODE_PERMISSIONS = 10;
    private final String[] REQUIRED_PERMISSIONS = new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private File outputDirectory;
    private String currentPhotoPath;
    private ImageButton capture_btns ,flipCameraBtn;;
    private int cameraFacing ;
    private Preview preview;
    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if (result) {
                startCamera();
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_camera);
        cameraFacing = CameraSelector.LENS_FACING_BACK;
        previewView = findViewById(R.id.customPreview);
        capture_btns = findViewById(R.id.capture_btn);
        okButton = findViewById(R.id.ok_btn);
        reTryButton = findViewById(R.id.retry_btn);
        capturedImage = findViewById(R.id.capture_image);
        flipCameraBtn = findViewById(R.id.flipCamera_btn);
        if (ContextCompat.checkSelfPermission(CustomCameraActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncher.launch(android.Manifest.permission.CAMERA);
        } else {
            startCamera();
        }

    }

    private void startCamera() {
        // Initialize your custom CameraX instance
        previewView.setVisibility(View.VISIBLE);
        capture_btns.setVisibility(View.VISIBLE);
        flipCameraBtn.setVisibility(View.VISIBLE);
        okButton.setVisibility(View.INVISIBLE);
        reTryButton.setVisibility(View.INVISIBLE);
        capturedImage.setVisibility(View.INVISIBLE);
        customCamera = new CustomCamera(previewView);
        customCamera.startCamera(this);
        capture_btns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });
        flipCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customCamera.filpCamera();
                customCamera.startCamera(CustomCameraActivity.this);
            }
        });

    }

    private void captureImage() {
        final File photoFile = new File(getExternalFilesDir(null), System.currentTimeMillis() + ".jpg");
        //File photoFile = createImageFile();
        if (photoFile != null) {
            ImageCapture.OnImageSavedCallback callback =  new ImageCapture.OnImageSavedCallback() {

                @Override
                public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                    runOnUiThread(() -> {
                        Uri imageUri= outputFileResults.getSavedUri();
                        previewView.setVisibility(View.INVISIBLE);
                        capture_btns.setVisibility(View.INVISIBLE);
                        flipCameraBtn.setVisibility(View.INVISIBLE);
                        okButton.setVisibility(View.VISIBLE);
                        reTryButton.setVisibility(View.VISIBLE);
                        capturedImage.setVisibility(View.VISIBLE);
                        capturedImage.setImageURI(imageUri);
                        okButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(CustomCameraActivity.this, "ImageSaved: " , Toast.LENGTH_SHORT).show();
                                Uri imageUri= outputFileResults.getSavedUri();
                                Intent resultIntent = new Intent();
                                resultIntent.setData(imageUri); // You can put any data you want
                                setResult(Activity.RESULT_OK, resultIntent);
                                finish();
                            }
                        });
                        reTryButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                startCamera();
                            }
                        });












                    });
                }

                @Override
                public void onError(@NonNull ImageCaptureException exception) {
                    // Handle error
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CustomCameraActivity.this, "Image saved at: " + photoFile.getPath(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            };
            customCamera.captureImage(photoFile, callback);
        }
    }
    private int aspectRatio(int width, int height) {
        double previewRatio = (double) Math.max(width, height) / Math.min(width, height);
        if (Math.abs(previewRatio - 4.0 / 3.0) <= Math.abs(previewRatio - 16.0 / 9.0)) {
            return AspectRatio.RATIO_4_3;
        }
        return AspectRatio.RATIO_16_9;
    }
    private File createImageFile() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(getExternalFilesDir(null), "CameraXImages");

        try {
            File image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );

            currentPhotoPath = image.getAbsolutePath();
            return image;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }


}