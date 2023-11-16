package com.example.team29project.View;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.team29project.R;
import com.example.team29project.View.MainActivity;


/**
 * A dialog to select a sorting method
 */
public class SortFragment extends DialogFragment {
    private OnFragmentInteractionListener listener;

    /**
     * Assigns the listeners in the dialog
     * @param dialog the context
     */
    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        ((MainActivity) getActivity()).setSortFragmentShown(false);
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

    /**
     * Creates a dialog and adds listeners
     * @param savedInstanceState The last saved instance state of the Fragment,
     * or null if this is a freshly created Fragment.
     *
     * @return the dialog
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
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
            Boolean isAsc = selectedId == R.id.sort_asc_radiobutton; // Check if the selected RadioButton is 'asc_radiobutton'

            listener.onSortConfirmPressed(sortBy, isAsc); // Call onConfirmPressed with the selected values
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
     * An interface for sort dialog listeners
     */
    public interface OnFragmentInteractionListener {
        void onSortConfirmPressed(String sortBy, Boolean isAsc);
    }
}
