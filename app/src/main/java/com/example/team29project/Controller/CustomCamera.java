package com.example.team29project.Controller;

import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import com.google.common.util.concurrent.ListenableFuture;
import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Represents a custom camera that can capture images and switch between front and back camera.
 */
public class CustomCamera {

    private PreviewView previewView;
    private ImageCapture imageCapture;
    private Executor executor = Executors.newSingleThreadExecutor();
    private int cameraFacing;
    ProcessCameraProvider cameraProvider;

    /**
     * Constructs a new CustomCamera with the given PreviewView.
     *
     * @param previewView The PreviewView where the camera preview will be displayed.
     */
    public CustomCamera(PreviewView previewView) {
        this.previewView = previewView;
        this.cameraFacing=CameraSelector.LENS_FACING_BACK;
    }

    /**
     * Flips the camera from back to front, or from front to back.
     */
    public void flipCamera(){
        if(this.cameraFacing == CameraSelector.LENS_FACING_BACK){
            this.cameraFacing= CameraSelector.LENS_FACING_FRONT;
        }
        else{
            this.cameraFacing=CameraSelector.LENS_FACING_BACK;
        }
    }

    /**
     * Starts the camera and binds it to the lifecycle of the given LifecycleOwner.
     *
     * @param lifecycleOwner The LifecycleOwner that the camera is bound to.
     */
    public void startCamera(LifecycleOwner lifecycleOwner) {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(previewView.getContext());

        cameraProviderFuture.addListener(() -> {
            try {
                 cameraProvider = cameraProviderFuture.get();

                // Set up the camera use cases
                bindCameraUseCases(lifecycleOwner);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(previewView.getContext()));
    }

    /**
     * Binds the camera use cases to the given ProcessCameraProvider and LifecycleOwner.
     * @param lifecycleOwner The LifecycleOwner that the camera use cases are bound to.
     */
    private void bindCameraUseCases(LifecycleOwner lifecycleOwner) {
        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(cameraFacing)
                .build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        imageCapture = new ImageCapture.Builder()
                    .build();
        cameraProvider.unbindAll();
        cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageCapture);
    }
    /**
     * Captures an image and saves it to the given file. If the image capture is not initialized, this method does nothing.
     *
     * @param outputFile The file where the image will be saved.
     * @param callback The callback that will be invoked after the image has been captured.
     */
    public void captureImage(File outputFile, ImageCapture.OnImageSavedCallback callback) {
        if (imageCapture != null) {
            ImageCapture.OutputFileOptions outputOptions = new ImageCapture.OutputFileOptions.Builder(outputFile).build();
            imageCapture.takePicture(outputOptions, executor, callback);
        }
    }
}





