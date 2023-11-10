package com.example.team29project;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Allow users to input/edit data for an inventory item
 * Validate and store the data collected
 * Interface with the Camera for photo capture
 */
public class InputFragment extends DialogFragment {

    private Item item;
    private EditText itemName;

    private EditText itemValue;
    private EditText itemDate;
    private EditText itemMake;
    private EditText itemModel;
    private EditText itemSerialNumber;

    private EditText itemDescription;
    private EditText itemComment;
    private int yearDate;
    private int monthDate;
    private int dayDate;
    private ArrayList<String> tags;

    /**
     * set current item to null and set tags
     * @param tags the tags to be used
     */
    public InputFragment(ArrayList<String> tags) {
        this.item = null;
        this.tags = tags;
    }

    /**
     * Set item and teags
     * @param aItem an item to be used
     * @param tags tags to be used
     */
    public InputFragment(Item aItem,ArrayList<String> tags) {
        this.item = aItem;
        this.tags = tags;
    }
    private OnFragmentsInteractionListener listener;

    /**
     * Interface for user interaction with fragments
     */
    public interface OnFragmentsInteractionListener {
        void onOKPressed(Item item);
        void onEditPressed(Item item);
        void onCancelPressed();

    }

    /**
     * Assign the FragmentInteractionListener
     * @param context FragmentInteractionListener
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentsInteractionListener) {
            listener = (OnFragmentsInteractionListener) context;
        } else {
            throw new RuntimeException(context + "OnFragmentInteractionListener is not implemented");
        }
    }

    /**
     * Create key listener to dismiss dialog when back key is released
     */
    @Override
    public void onResume() {
        super.onResume();
        // Handle with the backPressed button
        getDialog().setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                listener.onCancelPressed();
                dismiss();
                return true;
            }
            return false;
        });
    }


    /**
     * Create a dialog to edit the details of an item
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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_input, null);
        itemValue = view.findViewById(R.id.edit_item_value);
        itemName = view.findViewById(R.id.edit_item_name);
        itemMake  = view.findViewById(R.id.edit_item_make);
        itemDate = view.findViewById(R.id.edit_item_date);
        itemSerialNumber =  view.findViewById(R.id.edit_serialno);
        itemModel =  view.findViewById(R.id.edit_item_model);
        itemDescription =  view.findViewById(R.id.edit_description);
        itemComment = view.findViewById(R.id.edit_comment);
        if(item !=null){
            writeData(item);
        }

        DatePickerDialog.OnDateSetListener r = (view1, year, month, dayOfMonth) -> {
            yearDate = year;
            monthDate = month;
            dayDate = dayOfMonth;
            itemDate.setText(String.format("%d-%02d-%02d", yearDate,monthDate,dayDate));
        };
        itemDate.setOnClickListener(v -> {
            DatePicker dat = new DatePicker();
            dat.setListener(r);
            dat.show(getChildFragmentManager(),"DatePicker");
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        // TODO set up filter chips of all tags (from firestore) and select ones already tagged by the item

        // TODO set add tag and scan button listeners

        builder.setView(view);
        builder.setTitle("Add new item");
        // When cancel button pressed
        builder.setNegativeButton("Cancel", (dialog, which)->{
            listener.onCancelPressed();
        });
        builder.setPositiveButton("OK", (dialog, which) -> {
            try {
                String item_name = itemName.getText().toString();
                String item_date = itemDate.getText().toString();
                String item_make = itemMake.getText().toString();
                Double item_value = Double.parseDouble(itemValue.getText().toString());
                String item_serN = itemSerialNumber.getText().toString();
                String item_model = itemModel.getText().toString();
                String item_description = itemDescription.getText().toString();
                String item_comment = itemComment.getText().toString();
                // Check if it is empty except comment
                if(item_name.isEmpty() || item_date.isEmpty() || item_value.isNaN()||item_make.isEmpty() || item_model.isEmpty() || item_description.isEmpty()  || item_serN.isEmpty()){
                    throw new Exception();
                }
                // Check if it is future date
                if(yearDate>=currentYear){
                    if(monthDate> currentMonth+1|| monthDate== currentMonth+1 && dayDate >currentDay){
                        throw new IllegalArgumentException();
                    }
                }
                // If it is adding
                if (item == null) {
                    // TODO add tags selected to the item
                    // do this by checking the selected id from the chip group
                    listener.onOKPressed(new Item(item_name, item_date, item_value, item_make, item_model, item_description, item_comment, item_serN));
                }
                // if it is editing
                else {
                    item.setName(item_name);
                    item.setDate(item_date);
                    item.setValue(item_value);
                    item.setModel(item_model);
                    item.setDescription(item_description);
                    item.setSerialNumber(item_serN);
                    item.setMake(item_make);
                    item.setComment(item_comment);
                    listener.onEditPressed(item);
                }
            } catch(NumberFormatException e){
                Toast.makeText(getContext()," Wrong format of charges check again!",Toast.LENGTH_SHORT).show();
            }// Handle the case when dates.length() != 6
            catch (IllegalArgumentException e) {
                Toast.makeText(getContext(), "Wrong date. check again!", Toast.LENGTH_SHORT).show();
            }// If one of name,amount, date is empty
            catch(Exception e){
                Toast.makeText(getContext(),"Can't be empty!", Toast.LENGTH_SHORT).show();
            }
        });
        return builder.create();
    }

    /**
     * Get the item properties from a given item
     * @param item the item to collect data from
     */
    public void writeData(Item item){
        itemValue.setText(String.format("%.2f",item.getValue()));
        itemName.setText(item.getName());
        itemDate.setText(item.getDate());
        itemModel.setText(item.getDate());
        itemSerialNumber.setText(item.getSerialNumber());
        itemMake.setText(item.getMake());
        itemDescription.setText((item.getDescription()));
        itemComment.setText(item.getComment());

    }
}