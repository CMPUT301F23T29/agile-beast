package com.example.team29project;
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

public class CustomCamera {

    private PreviewView previewView;
    private ImageCapture imageCapture;
    private Executor executor = Executors.newSingleThreadExecutor();
    private int cameraFacing;

    public CustomCamera(PreviewView previewView) {
        this.previewView = previewView;
        this.cameraFacing=CameraSelector.LENS_FACING_BACK;
    }
    public void filpCamera(){
        if(this.cameraFacing == CameraSelector.LENS_FACING_BACK){
            this.cameraFacing= CameraSelector.LENS_FACING_FRONT;
        }
        else{
            this.cameraFacing=CameraSelector.LENS_FACING_BACK;
        }
    }

    public void startCamera(LifecycleOwner lifecycleOwner) {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(previewView.getContext());

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                // Set up the camera use cases
                bindCameraUseCases(cameraProvider, lifecycleOwner);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(previewView.getContext()));
    }

    private void bindCameraUseCases(ProcessCameraProvider cameraProvider, LifecycleOwner lifecycleOwner) {
        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(cameraFacing)
                .build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        imageCapture = new ImageCapture.Builder()
                .setTargetRotation(previewView.getDisplay().getRotation())
                .build();

        cameraProvider.unbindAll();
        cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageCapture);
    }

    public void captureImage(File outputFile, ImageCapture.OnImageSavedCallback callback) {
        if (imageCapture != null) {
            ImageCapture.OutputFileOptions outputOptions = new ImageCapture.OutputFileOptions.Builder(outputFile).build();
            imageCapture.takePicture(outputOptions, executor, callback);
        }
    }
}





