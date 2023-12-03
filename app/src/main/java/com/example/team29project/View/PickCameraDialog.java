package com.example.team29project.View;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.team29project.R;

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
     * creates the view with a set background colour
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.camera_or_gallery, container);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#D8E8EC")));
        return view;
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
        cameraPicked.setOnClickListener(v -> {
            listener.onCameraPressed();
            dismiss();

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
