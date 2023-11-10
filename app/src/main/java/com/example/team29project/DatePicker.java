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

/**
 * Helper class to allow a user to select a date using a popup dialog
 */
public class DatePicker extends DialogFragment {
    private DatePickerDialog.OnDateSetListener listener;
    public Calendar current = Calendar.getInstance();
    private Button okButton;
    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    /**
     * Creates a date picker dialog
     * @param savedInstanceState The last saved instance state of the Fragment, or null if this is a freshly created Fragment.
     *
     *
     * @return the DatePicker dialog
     */
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
        // Setting min/max value of month and year
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
        // Set the default value to current year, month, and day
       // Set the default value to current year, month, and day
monthPicker.setValue(current.get(Calendar.MONTH) + 1); // Months are 0-indexed in Calendar
year = current.get(Calendar.YEAR);
int maxDays = getMaxDaysInMonth(monthPicker.getValue(), year);
dayPicker.setMaxValue(maxDays);
dayPicker.setValue(current.get(Calendar.DAY_OF_MONTH));
        // Set the max year to be the current year since we do not want the future date
        yearPicker.setMaxValue(year);
        yearPicker.setValue(year);
        builder.setView(dialog);
        return builder.create();
    }

    /**
     * Gets the maximum days in a month
     * @param month the month to be used
     * @param year the year to be used
     * @return the maximum day in the provided month and year
     */
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

    /**
     * Checks if a year is a leap year
     * @param year the year to be used
     * @return a true or false value
     */
    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }


    public void setDate(Calendar instance) {
        current = instance;
    }
}
