package com.example.team29project.View;


import android.annotation.SuppressLint;
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
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

public class SerialActivity extends AppCompatActivity {

    private ImageView captureImageView;
    private TextView resultTextView;
    private Button snapButton, detectButton, cancelButton;
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

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_serial);
        captureImageView = findViewById(R.id.serial_image_view);
        resultTextView = findViewById(R.id.serial_text_view);
        snapButton = findViewById(R.id.snap_button);
        detectButton = findViewById(R.id.detect_button);
        cancelButton = findViewById(R.id.done_serial_button);

        Toast.makeText(this, "SerialActivity started", Toast.LENGTH_SHORT).show();

        if (ContextCompat.checkSelfPermission(SerialActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionActivityResultLauncher.launch(android.Manifest.permission.CAMERA);
        } else {
            captureImage();
        }

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                String resultText = resultTextView.getText().toString();
                resultIntent.putExtra("resultText", resultText);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

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
        if (serialBitmap == null) {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "trying to detect", Toast.LENGTH_SHORT).show();
        InputImage image = InputImage.fromBitmap(serialBitmap, 0);
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        recognizer.process(image)
                .addOnSuccessListener(result -> {
                    String resultText = result.getText();
                    resultTextView.setText(resultText);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to recognize text", Toast.LENGTH_SHORT).show();
                });

    }

    private void captureImage(){
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePicture.resolveActivity(getPackageManager())!=null){
            captureActivityResultLauncher.launch(takePicture);
        }
    }
}