package com.example.team29project.View;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.example.team29project.Controller.DatabaseController;
import com.example.team29project.Controller.OnScanListener;
import com.example.team29project.Controller.TagAddedItemCallback;
import com.example.team29project.Model.Item;
import com.example.team29project.Model.Tag;
import com.example.team29project.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

/**
 * Allow users to input/edit data for an inventory item
 * Validate and store the data collected
 * Interface with the Camera for photo capture
 */
public class InputFragment extends DialogFragment implements TagAddedItemCallback, OnScanListener {

    private Item item;
    private Button scanButton;
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

    private ChipGroup tagChips;
    private ArrayList<String> tags;
    private ArrayList<Tag> tempTags;
    DatabaseController db;


    /**
     * set current item to null and set tags
     */
    public InputFragment(DatabaseController db)  {
        this.item = null;
        this.db = db;
        this.tempTags= new ArrayList<>();
    }

    /**
     * Set item and tags
     * @param aItem an item to be used
     */
    public InputFragment(DatabaseController db, Item aItem) {
        this.item = aItem;
        this.db= db;
        this.tags= aItem.getTags();
        this.tempTags= new ArrayList<>();

    }
    private OnFragmentsInteractionListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input, container);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#D8E8EC")));
        return view;
    }

    /**
     * Callback function when tag is applied into item
     * @param tempTagList ArrayList of tag that selected by user
     */

    @Override
    public void onTagsApplied(ArrayList<Tag> tempTagList) {

        this.tempTags= tempTagList;

        drawTags(tagsToString());
    }

    @Override
    public void onScannedSerial(String scan) {
        itemSerialNumber.setText(scan);
    }

    @Override
    public void onScannedBarcode(String scan) {
        itemDescription.setText(scan);

    }

    /**
     * Interface for user interaction with fragments
     */
    public interface OnFragmentsInteractionListener {
        void onOKPressed();
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
        scanButton = view.findViewById(R.id.scan_button);
        tagChips = view.findViewById(R.id.tag_chip);
        if(item !=null){
            writeData(item);
        }

        DatePickerDialog.OnDateSetListener r = (view1, year, month, dayOfMonth) -> {
            yearDate = year;
            monthDate = month;
            dayDate = dayOfMonth;
            itemDate.setText(String.format("%d-%02d-%02d", yearDate,monthDate,dayDate));
        };
        itemDate.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                DatePicker dat = new DatePicker();
                dat.setListener(r);
                // Set the date to the current date
                dat.setDate(Calendar.getInstance());
                dat.show(getChildFragmentManager(), "DatePicker");
            }
            return true;
        });


        // When tag Chip is pressed
        tagChips.setOnClickListener(v -> new TagDialogue(db , InputFragment.this).show(getChildFragmentManager(),"Pick Tags"));

        // scanning camera
        scanButton.setOnClickListener(v -> new PickScanDialog().show(getChildFragmentManager(),"scan"));
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);
        // When cancel button pressed
        builder.setNegativeButton("Cancel", (dialog, which)-> listener.onCancelPressed());
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
                // update that the item is applied to this tag
                // Check if it is empty except comment
                if(item_name.isEmpty() || item_date.isEmpty() || item_value.isNaN()||item_make.isEmpty() || item_model.isEmpty() || item_description.isEmpty() ||item_comment.isEmpty()) {
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
                    String uniqueId = UUID.randomUUID().toString();
                    item=  new Item(item_name, item_date, item_value, item_make, item_model, item_description, item_comment, item_serN);
                    if(!this.tempTags.isEmpty()){
                        item.setTags(tagsToString());
                        for(Tag tag : this.tempTags) {
                            tag.addItem(uniqueId);
                            db.updateTag(tag);
                        }
                    }
                    db.addItem(item,uniqueId);
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
                    // add Delete Tag update
                    item.setTags(tagsToString());
                    for(String tag: this.tags){
                        db.removeTagfromItem(item,tag);
                    }
                    db.updateItem(item.getDocId(),item);
                    for(Tag tag : this.tempTags) {
                        tag.addItem(item.getDocId());
                        db.updateTag(tag);
                    }
                    listener.onOKPressed();
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
        drawTags(item.getTags());


    }
    public ArrayList<String> tagsToString(){
        ArrayList<String> temp = new ArrayList<>();
        for(Tag tag : this.tempTags){
            temp.add(tag.getName());
        }
        return temp;
    }


    /**
     * This function draws tag data into ChipGroup
     * @param tagString arrayList of String represents the tag
     */
    public void drawTags(ArrayList<String> tagString){
        tagChips.removeAllViews();
        for(String tag: tagString ){
            Chip chip = new Chip(getContext());
            chip.setText(tag);
            chip.setId(tagString.indexOf(tag));
            chip.setCheckable(false);
            chip.setClickable(false);
            chip.setChipBackgroundColorResource(R.color.background);
            chip.setTextColor(getResources().getColor(R.color.button_text, null));
            tagChips.addView(chip);
        }


    }
}