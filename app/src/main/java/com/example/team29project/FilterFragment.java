package com.example.team29project;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * A dialog to filter items on the main menu
 */
public class FilterFragment extends DialogFragment {
    private OnFragmentInteractionListener listener;

    /**
     * Assign the fragment listener if context is a valid instance f one, and display a success message to the user
     * @param context the context to be used
     */
    @Override
    public void onAttach(@NonNull Context context) {

        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            Toast.makeText(context, "attached successfully", Toast.LENGTH_SHORT).show();
            throw new RuntimeException(context + "OnFragmentInteractionListener is not implemented");
        }
    }

    /**
     * Initializes the filter activity
     * @param savedInstanceState The last saved instance state of the Fragment,
     * or null if this is a freshly created Fragment.
     *
     * @return the dialog
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_filter, null);

        Button confirm = view.findViewById(R.id.confirm_filter_button);
        Button cancel = view.findViewById(R.id.cancel_filter_button);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // Set OnClickListener for confirm button
        confirm.setOnClickListener(v -> {
            String filterBy="",data="";
            listener.onFilterConfirmPressed(filterBy, data); // Call onConfirmPressed with the selected values
            dismiss(); // Close the dialog
        });
        // Set OnClickListener for cancel button
        cancel.setOnClickListener(v -> {
            dismiss(); // Close the dialog
        });

        return builder
                .setView(view)
                .create();
    }

    /**
     * An interface containing a method for when a new filter should be applied
     */
    public interface OnFragmentInteractionListener {
        void onFilterConfirmPressed(String filterBy, String data);
    }
}
