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

public class SortFragment extends DialogFragment {
    private OnFragmentInteractionListener listener;

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
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Toast.makeText(getContext(), " dialog successfully", Toast.LENGTH_SHORT).show();

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_sort, null);

        Spinner sortSpinner = view.findViewById(R.id.sort_spinner);
        RadioGroup sortOrder = view.findViewById(R.id.sort_radiogroup);
        Button confirm = view.findViewById(R.id.confirm_sort_button);
        Button cancel = view.findViewById(R.id.cancel_sort_button);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // Set OnClickListener for confirm button
        confirm.setOnClickListener(v -> {
            String sortBy = sortSpinner.getSelectedItem().toString(); // Get selected item from Spinner
            int selectedId = sortOrder.getCheckedRadioButtonId(); // Get selected RadioButton from RadioGroup
            int isAsc = selectedId == R.id.sort_asc_radiobutton ? 1 : 0; // Check if the selected RadioButton is 'asc_radiobutton'

            listener.onConfirmPressed(sortBy, isAsc); // Call onConfirmPressed with the selected values
            dismiss(); // Close the dialog
        });

        // Set OnClickListener for cancel button
        cancel.setOnClickListener(v -> {
            listener.onCancelPressed(); // Call onCancelPressed
            dismiss(); // Close the dialog
        });

        return builder
                .setView(view)
                .create();
    }

    public interface OnFragmentInteractionListener {
        void onConfirmPressed(String sortBy, int isAsc);
        void onCancelPressed();
    }
}
