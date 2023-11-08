package com.example.team29project;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.w3c.dom.Text;

public class PickCameraDialog extends DialogFragment {
    private TextView camera_picked , gallery_picked;
    private ImageOrGalleryListener listener;

    public interface ImageOrGalleryListener {
        void onGalleryPressed();

        void onCameraPressed();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof InputFragment.OnFragmentInteractionListener) {
            listener = (ImageOrGalleryListener) context;
        } else {
            throw new RuntimeException(context + "ImageOrGallery Listener is not implemented");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.camera_or_gallery, null);
        camera_picked = view.findViewById(R.id.choose_camera);
        gallery_picked = view.findViewById(R.id.choose_gallery);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);
        builder.setTitle("Pick Option");
        camera_picked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCameraPressed();
                dismiss();

            }
        });
        gallery_picked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onGalleryPressed();
                dismiss();

            }
        });
        return builder.create();

    }
}
