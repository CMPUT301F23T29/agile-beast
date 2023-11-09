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

/**
 * Represents an activity for displaying images. The activity allows the user to take a photo with the camera or pick a photo from the gallery.
 *
 * @see AppCompatActivity
 */
public class DisplayImageActivity extends AppCompatActivity {
    private ImageView images;
    private Button camera_btn;
    private Button gallery_btn;
    /**
     * An ActivityResultLauncher for handling the result of the image capture or image pick activity.
     */
    ActivityResultLauncher<Intent> pictureActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                /**
                 * Handles the result of the image capture or image pick activity.
                 *
                 * @param result The result of the activity.
                 */
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        images.setImageURI(data.getData());
                    }
                }
            });
    
    /**
     * Initializes the activity, sets up the image view and the camera and gallery buttons.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down, this contains the data it most recently supplied in onSaveInstanceState(Bundle). Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagedisplay);
        images = findViewById(R.id.image_pic);
        camera_btn = findViewById(R.id.takePhoto_btn);
        gallery_btn = findViewById((R.id.pickPhoto_btn));
        /**
         * Handles the click event of the gallery button. This opens the gallery for the user to pick an image.
         *
         * @param v The view that was clicked.
         */
        gallery_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery_intent = new Intent(Intent.ACTION_PICK);
                gallery_intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                gallery_intent.setAction(Intent.ACTION_PICK);
                pictureActivityResultLauncher.launch(gallery_intent);

            }
        });
        /**
         * Handles the click event of the camera button. This opens the custom camera activity for the user to take a photo.
         *
         * @param v The view that was clicked.
         */
        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent customCamera = new Intent(DisplayImageActivity.this, CustomCameraActivity.class);
                pictureActivityResultLauncher.launch(customCamera);
            }
        });

    }
}