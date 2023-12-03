package com.example.team29project.View;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.example.team29project.Controller.DatabaseController;
import com.example.team29project.Controller.OnDeletedImageCallback;
import com.example.team29project.R;
import com.google.firebase.storage.StorageReference;

/**
 * Dialog class that display Images that user uploaded. Extends DialogFragment
 */

public class ImageDisplayDialog extends DialogFragment {
    private String photo;
    private DatabaseController db;

    private OnDeletedImageCallback listener;
    private int position;

    private Context context;

    /**
     *
     * @param db database controller instance
     * @param photoString unique ID of photo selected
     * @param position position of photo from photoList
     */
    public ImageDisplayDialog(DatabaseController db,String photoString , int position){
        this.photo = photoString;
        this.db = db;
        this.position= position;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        this.context= context;
        super.onAttach(context);
        if (context instanceof InputFragment.OnFragmentsInteractionListener) {
            listener = (OnDeletedImageCallback) context;
        } else {
            throw new RuntimeException(context + "OnDeletedImageCallback is not implemented");
        }
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.image_display, null);
        ImageView image = view.findViewById(R.id. imageview_display);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);
        Button ok= view.findViewById(R.id.ok_image_button);
        Button delete = view.findViewById(R.id.delete_Image);
        StorageReference dateRef = db.getImageRef().child("images/" +photo);
        dateRef.getDownloadUrl().addOnSuccessListener(downloadUrl -> Glide.with(this.context)
                .load(downloadUrl)
                .into(image));
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onImageDeleted(photo,position);
                dismiss();
            }
        });
        return builder.create();

    }
}
