package com.example.team29project;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Represents a custom camera activity. This activity allows the user to capture images using the device's camera.
 */
public class CustomCameraActivity extends AppCompatActivity {
    private CustomCamera customCamera;
    private PreviewView previewView;
    private ImageView capturedImage;
    private Button okButton;
    private Button reTryButton;

    private ImageButton captureBtn,flipCameraBtn;

    // Asking for Permission to capture Camera
    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
        if (result) {
            startCamera();
        }
    });


    /**
     * Initializes the activity, sets up the camera and the buttons.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down, this contains the data it most recently supplied in onSaveInstanceState(Bundle). Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_camera);
        int cameraFacing = CameraSelector.LENS_FACING_BACK;
        previewView = findViewById(R.id.customPreview);
        captureBtn = findViewById(R.id.capture_btn);
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

    /**
     * Starts the camera and sets up the capture and flip camera buttons.
     */
    private void startCamera() {
        // Initialize your custom CameraX instance
        previewView.setVisibility(View.VISIBLE);
        captureBtn.setVisibility(View.VISIBLE);
        flipCameraBtn.setVisibility(View.VISIBLE);
        okButton.setVisibility(View.INVISIBLE);
        reTryButton.setVisibility(View.INVISIBLE);
        capturedImage.setVisibility(View.INVISIBLE);
        customCamera = new CustomCamera(previewView);
        customCamera.startCamera(this);
        captureBtn.setOnClickListener(v -> captureImage());
        flipCameraBtn.setOnClickListener(view -> {
            customCamera.flipCamera();
            customCamera.startCamera(CustomCameraActivity.this);
        });

    }

    /**
     *
     * Starts the camera and sets up the capture and flip camera buttons.
     *
     */
    private void captureImage() {
        final File photoFile = new File(getExternalFilesDir(null), System.currentTimeMillis() + ".jpg");
        //File photoFile = createImageFile();

        ImageCapture.OnImageSavedCallback callback = new ImageCapture.OnImageSavedCallback() {
               /**
                 * Sets buttons to visible or invisible for an image
                 * @param outputFileResults the image that was taken
                 */

            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                runOnUiThread(() -> {
                    Uri imageUri = outputFileResults.getSavedUri();
                    previewView.setVisibility(View.INVISIBLE);
                    captureBtn.setVisibility(View.INVISIBLE);
                    flipCameraBtn.setVisibility(View.INVISIBLE);
                    okButton.setVisibility(View.VISIBLE);
                    reTryButton.setVisibility(View.VISIBLE);
                    capturedImage.setVisibility(View.VISIBLE);
                    capturedImage.setImageURI(imageUri);
                    okButton.setOnClickListener(v -> {
                        Toast.makeText(CustomCameraActivity.this, "ImageSaved: ", Toast.LENGTH_SHORT).show();
                        Uri imageUri1 = outputFileResults.getSavedUri();
                        Intent resultIntent = new Intent();
                        resultIntent.setData(imageUri1);
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();

                    });
                    reTryButton.setOnClickListener(v -> startCamera());
                });
            }
            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                // Handle error
                runOnUiThread(() -> Toast.makeText(CustomCameraActivity.this, "Image saved at: " + photoFile.getPath(), Toast.LENGTH_SHORT).show());

            }
        };
        customCamera.captureImage(photoFile, callback);
    }

    /**
     *
     * @param height the height of the screen on camera(Y axis)
     * @param width the width of the screen on camera(X axis)
     * @return AspectRatio that the ratio of camera in scale
     */
    private int aspectRatio(int width, int height) {
        double previewRatio = (double) Math.max(width, height) / Math.min(width, height);
        if (Math.abs(previewRatio - 4.0 / 3.0) <= Math.abs(previewRatio - 16.0 / 9.0)) {
            return AspectRatio.RATIO_4_3;
        }
        return AspectRatio.RATIO_16_9;
    }

    /**
     * Creates an image file with a unique name in the "CameraXImages" directory.
     *
     * @return The created image file.
     */
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

            String currentPhotoPath = image.getAbsolutePath();
            return image;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }


}