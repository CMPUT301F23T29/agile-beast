package com.example.team29project;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {
    public static final int RESULT_CODE = 22;
    Button cameraBtn;
    ImageView image;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cameraBtn = findViewById(R.id.camera_button);
        image = findViewById(R.id.image_view);
        ActivityResultLauncher<Intent> launchers = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    Intent data = result.getData();
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        image.setImageBitmap(photo);


                    }
                    else{
                        Toast.makeText(this,"Nah",Toast.LENGTH_SHORT).show();
                        super.onActivityResult(result.getResultCode(),Activity.RESULT_OK,data);
                    }
                });
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                launchers.launch(camera);
            }

        });

    }
}