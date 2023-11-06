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
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import com.google.common.util.concurrent.ListenableFuture;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

/**
 * CameraActivity is an activity for handling camera operations.
 */
public class CameraActivity extends AppCompatActivity {
    ImageButton capture, flipCamera;
    private PreviewView previewView;
    int cameraFacing = CameraSelector.LENS_FACING_BACK;
    // This is an ActivityResultLauncher that handles the request permission result for camera permission.
    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        /**
         * Handles the result of a permission request. If the permission is granted, it starts the camera.
         */
        public void onActivityResult(Boolean result) {
            if (result) {
                startCamera(cameraFacing);
            }
        }
    });

    /**
     * Initializes the camera activity, checks for camera permission, and sets up the flip camera button.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down, 
     * this contains the data it most recently supplied in onSaveInstanceState. Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        previewView = findViewById(R.id.cameraPreview);
        capture = findViewById(R.id.capture);
        flipCamera = findViewById(R.id.flipCamera);

        // Check if the application has the camera permission, if not, request it, otherwise start the camera.
        if (ContextCompat.checkSelfPermission(CameraActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncher.launch(android.Manifest.permission.CAMERA);
        } else {
            startCamera(cameraFacing);
        }

        // Set up the click listener for the flip camera button.
        flipCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cameraFacing == CameraSelector.LENS_FACING_BACK) {
                    cameraFacing = CameraSelector.LENS_FACING_FRONT;
                } else {
                    cameraFacing = CameraSelector.LENS_FACING_BACK;
                }
                startCamera(cameraFacing);
            }
        });
    }

    /**
     * Starts the camera with the specified camera facing.
     * Also sets up a click listener for the capture button.
     *
     * @param cameraFacing The camera facing to use (CameraSelector.LENS_FACING_FRONT or CameraSelector.LENS_FACING_BACK).
     */
        public void startCamera(int cameraFacing) {
        int aspectRatio = aspectRatio(previewView.getWidth(), previewView.getHeight());
        ListenableFuture<ProcessCameraProvider> listenableFuture = ProcessCameraProvider.getInstance(this);

        listenableFuture.addListener(() -> {
            try {
                //Gets an instance of the ProcessCameraProvider(manages the lifecycle of camera sessions)
                ProcessCameraProvider cameraProvider = (ProcessCameraProvider) listenableFuture.get();
                //Builds a Preview and ImageCapture instance with the desired configurations.
                Preview preview = new Preview.Builder().setTargetAspectRatio(aspectRatio).build();
                ImageCapture imageCapture = new ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .setTargetRotation(getWindowManager().getDefaultDisplay().getRotation()).build();
                //Builds a CameraSelector (select the camera lens based on 'cameraFacing' variable)
                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(cameraFacing).build();
                //Bound camera provider to the lifecycle of this activity 
                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
                // A click listener is also set on the capture button to handle image capture when the button is clicked.
                capture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //gets permision to store the photo
                        if (ContextCompat.checkSelfPermission(CameraActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            activityResultLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        }
                        takePicture(imageCapture);
                    }
                });
                // Connect the preview use case to the previewView
                preview.setSurfaceProvider(previewView.getSurfaceProvider());
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    /**
     * Takes a picture using the provided ImageCapture.
     * If the image is saved successfully, a toast is displayed and the DisplayImageActivity is started.
     * If there's an error, a toast is displayed and the camera is restarted.
     *
     * @param imageCapture The ImageCapture to use to take the picture.
     */
    public void takePicture(ImageCapture imageCapture) {
        // Create a new file for the image
        final File file = createImageFile();
        // Take a picture and save it to the file
        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file).build();
        imageCapture.takePicture( outputFileOptions, Executors.newCachedThreadPool(), new ImageCapture.OnImageSavedCallback() {
            
            /**
             * This method is called when the image is successfully saved. It displays a toast message, gets the URI of the saved image,
             * creates an intent to start DisplayImageActivity, puts the image URI into the intent's extras, and starts the activity.
             *
             * @param outputFileResults The results of the image capture, which include the saved URI.
             */
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                runOnUiThread(() -> {
                    Toast.makeText(CameraActivity.this, "ImageSaved: " , Toast.LENGTH_SHORT).show();
                    Uri imageUri= outputFileResults.getSavedUri();

                    Intent display_image = new Intent(CameraActivity.this ,DisplayImageActivity.class);
                    display_image.putExtra("imageUri", imageUri.toString());
                    startActivity(display_image);
                });

            }

            /**
             * This method is called when there's an error saving the image. It displays a toast message with the error message and 
             * restarts the camera.
             * @param exception The exception thrown during image capture.
             */
            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                runOnUiThread(() -> Toast.makeText(CameraActivity.this, "Failed to save: " + exception.getMessage(), Toast.LENGTH_SHORT).show());
                startCamera(cameraFacing);
            }
        });
    }

    /**
     * Determines the aspect ratio for an image based on its width and height.
     *
     * @param width The width of the image.
     * @param height The height of the image.
     * @return The aspect ratio of the image as an int.
     */
    private int aspectRatio(int width, int height) {
        double previewRatio = (double) Math.max(width, height) / Math.min(width, height);
        if (Math.abs(previewRatio - 4.0 / 3.0) <= Math.abs(previewRatio - 16.0 / 9.0)) {
            return AspectRatio.RATIO_4_3;
        }
        return AspectRatio.RATIO_16_9;
    }

    /**
     * Creates a new image file in the "Pictures" directory.
     * The file name is based on the current timestamp.
     *
     * @return The new image file.
     */
    private File createImageFile() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir =  getExternalFilesDir("Pictures");
        try {
            File image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
            return image;
        } catch (Exception ex) {
            Toast.makeText(this, "sss", Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
            return null;
        }

    }

}