package com.example.team29project;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class FilterFragment extends DialogFragment {
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
            String filterBy = filterSpinner.getSelectedItem().toString(); // Get selected item from Spinner
            String data="";
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

    public interface OnFragmentInteractionListener {
        void onFilterConfirmPressed(String filterBy, String data);
    }
}
