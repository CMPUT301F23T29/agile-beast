package com.example.team29project;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


import com.example.team29project.MainActivity;

import java.util.Calendar;


/**
 * A dialog to filter items on the main menu
 */

public class FilterFragment extends DialogFragment {
    private OnFragmentInteractionListener listener;
    private String selectedItem ="default";

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        ((MainActivity) getActivity()).setFilterFragmentShown(false);
    }

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

        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_filter, null);
        Button confirm = view.findViewById(R.id.confirm_filter_button);
        Button cancel = view.findViewById(R.id.cancel_filter_button);
        EditText make = view.findViewById(R.id.filter_by_make_editview);
        EditText startDate = view.findViewById(R.id.filter_by_start_date_editview);
        EditText endDate = view.findViewById(R.id.filter_by_end_date_editview);
        EditText description = view.findViewById(R.id.filter_by_description_editview);
        Spinner filterSpinner = view.findViewById(R.id.filter_spinner);
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Code to execute when an item is selected
                selectedItem = parent.getItemAtPosition(position).toString().toLowerCase();
                switch (selectedItem) {
                    case "make":
                        make.setVisibility(View.VISIBLE);
                        startDate.setVisibility(View.GONE);
                        endDate.setVisibility(View.GONE);
                        description.setVisibility(View.GONE);
                        break;
                    case "date":
                        make.setVisibility(View.GONE);
                        startDate.setVisibility(View.VISIBLE);
                        endDate.setVisibility(View.VISIBLE);
                        description.setVisibility(View.GONE);
                        break;
                    case "description":
                        make.setVisibility(View.GONE);
                        startDate.setVisibility(View.GONE);
                        endDate.setVisibility(View.GONE);
                        description.setVisibility(View.VISIBLE);
                        break;
                    default:
                        make.setVisibility(View.GONE);
                        startDate.setVisibility(View.GONE);
                        endDate.setVisibility(View.GONE);
                        description.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                make.setVisibility(View.GONE);
                startDate.setVisibility(View.GONE);
                endDate.setVisibility(View.GONE);
                description.setVisibility(View.GONE);
            }
        });

        DatePickerDialog.OnDateSetListener startDatePicker = (view1, year, month, dayOfMonth) -> {
            int yearDate = year;
            int monthDate = month;
            int dayDate = dayOfMonth;
            startDate.setText(String.format("%d-%02d-%02d", yearDate,monthDate,dayDate));
        };

        startDate.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                DatePicker dat = new DatePicker();
                dat.setListener(startDatePicker);
                dat.show(getChildFragmentManager(), "DatePicker");
            }
            return true;
        });

        DatePickerDialog.OnDateSetListener EndDatePicker = (view1, year, month, dayOfMonth) -> {
            int yearDate = year;
            int monthDate = month;
            int dayDate = dayOfMonth;
            endDate.setText(String.format("%d-%02d-%02d", yearDate,monthDate,dayDate));
        };

        endDate.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                DatePicker dat = new DatePicker();
                dat.setListener(EndDatePicker);
                dat.show(getChildFragmentManager(), "DatePicker");
            }
            return true;
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // Set OnClickListener for confirm button
        confirm.setOnClickListener(v -> {
            String filterBy = selectedItem; // Get the selected filter
            String data="";
            //TODO: data validation
            switch (filterBy) {
                case "make":
                    data = make.getText().toString();
                    break;
                case "date":
                    data = startDate.getText().toString() + "," + endDate.getText().toString();
                    break;
                case "description":
                    data = description.getText().toString();
                    break;
                default:
                    filterBy="default";
                    data="";
                    break;
            }
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
