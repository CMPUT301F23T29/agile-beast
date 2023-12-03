package com.example.team29project.View;

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
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.example.team29project.Controller.DatabaseController;
import com.example.team29project.Controller.TagAdapter;
import com.example.team29project.Controller.TagModifyCallback;
import com.example.team29project.R;


/**
 * A dialog to filter items on the main menu
 */

public class FilterFragment extends DialogFragment implements TagModifyCallback {
    private OnFragmentInteractionListener listener;
    private String selectedItem ="default";
    private TagAdapter tagAdapter;
    private final DatabaseController db;

    public FilterFragment(DatabaseController db) {
        this.db = db;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        ((MainPageActivity) getActivity()).setFilterFragmentShown(false);
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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_filter, null);

        tagAdapter = new TagAdapter(getContext(), db.getTags());
        db.loadInitialTags(this);
        Button confirm = view.findViewById(R.id.confirm_filter_button);
        Button cancel = view.findViewById(R.id.cancel_filter_button);
        EditText make = view.findViewById(R.id.filter_by_make_editview);
        EditText startDate = view.findViewById(R.id.filter_by_start_date_editview);
        EditText endDate = view.findViewById(R.id.filter_by_end_date_editview);
        EditText description = view.findViewById(R.id.filter_by_description_editview);
        Spinner filterSpinner = view.findViewById(R.id.filter_spinner);
        Spinner tagSpinner = view.findViewById(R.id.filter_by_tags);
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Code to execute when an item is selected
                selectedItem = parent.getItemAtPosition(position).toString().toLowerCase();
                make.setVisibility(View.GONE);
                startDate.setVisibility(View.GONE);
                endDate.setVisibility(View.GONE);
                description.setVisibility(View.GONE);
                tagSpinner.setVisibility(View.GONE);
                switch (selectedItem) {
                    case "make":
                        make.setVisibility(View.VISIBLE);
                        break;
                    case "date":
                        startDate.setVisibility(View.VISIBLE);
                        endDate.setVisibility(View.VISIBLE);
                        break;
                    case "description":
                        description.setVisibility(View.VISIBLE);
                        break;
                    case "tags":
                        tagSpinner.setVisibility(View.VISIBLE);
                        tagSpinner.setAdapter(tagAdapter);
                        break;
                    default:
                        break;
                }
            }

            /**
             * When nothing is being selected
             * @param adapterView The AdapterView that now contains no selected item.
             */

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                make.setVisibility(View.GONE);
                startDate.setVisibility(View.GONE);
                endDate.setVisibility(View.GONE);
                description.setVisibility(View.GONE);
            }
        });

        DatePickerDialog.OnDateSetListener startDatePicker = (view1, year, month, dayOfMonth) -> startDate.setText(String.format("%d-%02d-%02d", year, month, dayOfMonth));

        startDate.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                DatePicker dat = new DatePicker();
                dat.setListener(startDatePicker);
                dat.show(getChildFragmentManager(), "DatePicker");
            }
            return true;
        });

        DatePickerDialog.OnDateSetListener EndDatePicker = (view1, year, month, dayOfMonth) -> endDate.setText(String.format("%d-%02d-%02d", year, month, dayOfMonth));

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
            String data;
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
                case "tags":
                    if (tagSpinner.getCount() > 0) {
                        data = tagSpinner.getSelectedItem().toString();
                    } else {
                        data = "";
                    }
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

    @Override
    public void onTagModified() {
        tagAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTagsLoaded() {
        tagAdapter.notifyDataSetChanged();
    }

    /**
     * An interface containing a method for when a new filter should be applied
     */
    public interface OnFragmentInteractionListener {
        void onFilterConfirmPressed(String filterBy, String data);
    }
}
