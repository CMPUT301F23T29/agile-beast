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
import android.widget.EditText;

/**
 * Allow users to input/edit data for an inventory item
 * Validate and store the data collected
 * Interface with the Camera for photo capture
 */
public class InputFragment extends DialogFragment {

    private OnFragmentInteractionListener listener;
    private Item item;
    private EditText itemName;

    private EditText itemValue;
    private EditText itemDate;
    private EditText itemMake;
    private EditText itemModel;
    private EditText itemSerialNumber;

    private EditText itemDescription;
    private EditText itemComment;


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
        itemValue = view.findViewById(R.id.edit_item_value);
        itemName = view.findViewById(R.id.edit_item_name);
        itemMake  = view.findViewById(R.id.edit_item_make);
        itemDate = view.findViewById(R.id.edit_item_date);
        itemSerialNumber =  view.findViewById(R.id.edit_serialno);
        itemModel =  view.findViewById(R.id.edit_item_model);
        itemDescription =  view.findViewById(R.id.edit_description);
        itemComment = view.findViewById(R.id.edit_comment);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());


        // TODO set up filter chips of all tags (from firestore) and select ones already tagged by the item

        // TODO set scan button listener

        builder.setView(view);
        builder.setTitle("Add new item");
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("OK", (dialog, which) -> {
            String item_name = itemName.getText().toString();
            String item_date = itemDate.getText().toString();
            String item_make = itemMake.getText().toString();
            String item_value = itemValue.getText().toString();
            String item_serN = itemSerialNumber.getText().toString();
            String item_model = itemModel.getText().toString();
            String item_description = itemDescription.getText().toString();
            String item_comment = itemComment.getText().toString();

            Item new_item = new Item(item_name,item_date,item_value,item_make,item_model, item_description,item_comment,item_serN);
            // TODO add tags selected to the item
            // do this by checking the selected id from the chip group

            listener.onOKPressed(new_item);
        });
        return builder.create();
    }
}