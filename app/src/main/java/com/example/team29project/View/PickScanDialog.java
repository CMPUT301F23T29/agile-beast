package com.example.team29project.View;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.example.team29project.Controller.OnScanListener;
import com.example.team29project.R;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import java.io.FileNotFoundException;


/**
 * A dialog to pick scan option. One for Barcode and
 */
public class PickScanDialog extends DialogFragment {
    private TextView barcodePicked, serialPicked;

    private OnScanListener callback;

    // Receive image Uri from CustomCameraActivity convert it into Bitmap and proceed text recognition.
    ActivityResultLauncher<Intent> scanSerialLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent data = result.getData();
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContext()
                                .getContentResolver().openInputStream(data.getData()));

                        InputImage image = InputImage.fromBitmap(bitmap, 0);
                        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
                        recognizer.process(image).addOnSuccessListener(text -> callback.onScannedSerial(text.getText())).addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed to recognize text", Toast.LENGTH_SHORT).show());

                    } catch (FileNotFoundException e) {
                        Toast.makeText(getActivity(), "failed", Toast.LENGTH_SHORT).show();
                        throw new RuntimeException(e);
                    }
                    dismiss();

                }
            });


    /**
     * Assign the listener
     * @param context assigns the listener for the user to decide between barcode or serial
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            callback = (OnScanListener) getParentFragment();
        } catch (ClassCastException e) {
            assert getParentFragment() != null;
            throw new ClassCastException(getParentFragment()+ " must implement OnScanListener");
        }

    }

    /**
     * Create the dialog and initialize user input listeners
     * @param savedInstanceState The last saved instance state of the Fragment,
     * or null if this is a freshly created Fragment.
     *
     * @return the dialog
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.barcode_or_serial, null);
        barcodePicked = view.findViewById(R.id.choose_barcode);
        serialPicked = view.findViewById(R.id.choose_serial);
        GmsBarcodeScannerOptions options = new GmsBarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
                .enableAutoZoom()
                .build();
        GmsBarcodeScanner scanner = GmsBarcodeScanning.getClient(getContext(),options);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);
        builder.setTitle("Pick what you want to scan");

        // Scan Barcode
        barcodePicked.setOnClickListener(v -> {
            scanner
                    .startScan()
                    .addOnSuccessListener(
                            barcode -> {
                                String rawValue = barcode.getRawValue();
                                callback.onScannedBarcode(rawValue);
                            })
                    .addOnFailureListener(
                            e -> Toast.makeText(getActivity(), "Failed to detect a barcode", Toast.LENGTH_SHORT).show());
            dismiss();
        });
        serialPicked.setOnClickListener(new View.OnClickListener() {
            /**
             * Handles the click event
             * @param v the view to be used
             */
            @Override
            public void onClick(View v) {
                Intent scan = new Intent(getActivity(),CustomCameraActivity.class);
                scanSerialLauncher.launch(scan);
            }
        });
        return builder.create();
    }
}