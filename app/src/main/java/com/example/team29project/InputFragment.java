package com.example.team29project;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Allow users to input/edit data for an inventory item
 * Validate and store the data collected
 * Interface with the Camera for photo capture
 */
public class InputFragment extends DialogFragment {

    private OnFragmentInteractionListener listener;
    private Item item;

    public InputFragment() {
        this.item = null;
    }

    public InputFragment(Item aItem) {
        this.item = aItem;
    }

    public interface OnFragmentInteractionListener {
        void onOKPressed(Item item);
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context + "OnFragmentInteractionListener is not implemented");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_input, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // TODO get all texts

        // TODO set add tag and scan button listeners

        return builder
                .setView(view)
                .setTitle("Add new item")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", (dialog, which) -> {
                    // TODO
                    listener.onOKPressed(new Item());
                }).create();
    }
}