package com.example.team29project;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * A dialog to pick between the gallery or camera
 */
public class PickCameraDialog extends DialogFragment {
    private TextView cameraPicked, galleryPicked;
    private ImageOrGalleryListener listener;

    /**
     * An interface for user input callbacks
     */
    public interface ImageOrGalleryListener {
        void onGalleryPressed();

        void onCameraPressed();
    }

    /**
     * Assign the image gallery listener
     * @param context assigns the listener for the user to decide between camera or gallery
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ImageOrGalleryListener) {
            listener = (ImageOrGalleryListener) context;
        } else {
            throw new RuntimeException(context + "ImageOrGallery Listener is not implemented");
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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.camera_or_gallery, null);
        cameraPicked = view.findViewById(R.id.choose_camera);
        galleryPicked = view.findViewById(R.id.choose_gallery);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);
        builder.setTitle("Pick Option");
        cameraPicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCameraPressed();
                dismiss();

            }
        });
        galleryPicked.setOnClickListener(new View.OnClickListener() {
            /**
             * Handles the click event
             * @param v the view to be used
             */
            @Override
            public void onClick(View v) {
                listener.onGalleryPressed();
                dismiss();
            }
        });
        return builder.create();
    }
}
