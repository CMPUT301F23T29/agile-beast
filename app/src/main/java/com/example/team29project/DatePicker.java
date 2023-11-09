package com.example.team29project;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePicker extends DialogFragment {
    private DatePickerDialog.OnDateSetListener listener;
    public Calendar current = Calendar.getInstance();
    private Button okButton;
    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int year;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialog = inflater.inflate(R.layout.date_pick, null);
        okButton = dialog.findViewById(R.id.ok__pick_button);
        // YearPicker
        final NumberPicker monthPicker =  dialog.findViewById(R.id.picker_month);
        // MonthPicker
        final NumberPicker yearPicker =  dialog.findViewById(R.id.picker_year);
        final NumberPicker dayPicker = dialog.findViewById(R.id.picker_date);
        dayPicker.setMinValue(1);
        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        yearPicker.setMinValue(1900);
        monthPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            int selectedMonth = newVal;
            int selectedYear = yearPicker.getValue();
            int maxDays = getMaxDaysInMonth(selectedMonth, selectedYear);
            dayPicker.setMaxValue(maxDays);
        });
        // Bundle of date data that will be sent to CustomDialogue class when it pressed ok
        okButton.setOnClickListener(view -> {
            listener.onDateSet(null, yearPicker.getValue(), monthPicker.getValue(), dayPicker.getValue());
            DatePicker.this.getDialog().cancel();
        });
        // Setting min/max value of month

        // Set the default value to current year nad month
        monthPicker.setValue(1);
        dayPicker.setValue(1);
        year = current.get(Calendar.YEAR);
        // Set the max year to be the current year since we do not want the future date
        yearPicker.setMaxValue(year);
        yearPicker.setValue(year);
        builder.setView(dialog);
        return builder.create();
    }
    private int getMaxDaysInMonth(int month, int year) {
        if (month == 2) {  // February
            if (isLeapYear(year)) {
                return 29;
            } else {
                return 28;
            }
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        } else {
            return 31;
        }
    }
    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }




}
