package com.example.team29project.View;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.team29project.R;

/**
 * A dialog to pick between the gallery or camera
 */
public class PickScanDialog extends DialogFragment {
    private TextView barcodePicked, serialPicked;
    private BarcodeOrSerialListener listener;

    /**
     * An interface for user input callbacks
     */
    public interface BarcodeOrSerialListener {
        void onBarcodePressed();

        void onSerialPressed();
    }

    /**
     * Assign the listener
     * @param context assigns the listener for the user to decide between barcode or serial
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof BarcodeOrSerialListener) {
            listener = (BarcodeOrSerialListener) context;
        } else {
            throw new RuntimeException(context + "BarcodeOrSerial Listener is not implemented");
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);
        builder.setTitle("Pick what you want to scan");
        barcodePicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onBarcodePressed();
                dismiss();
            }
        });
        serialPicked.setOnClickListener(new View.OnClickListener() {
            /**
             * Handles the click event
             * @param v the view to be used
             */
            @Override
            public void onClick(View v) {
                listener.onSerialPressed();
                dismiss();
            }
        });
        return builder.create();
    }
}
