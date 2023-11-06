package com.example.team29project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;

/**
 * Display details of a selected inventory item
 * Allow users to view and potentially edit the item's information
 */
public class DisplayActivity extends AppCompatActivity implements InputFragment.OnFragmentInteractionListener {

    private Button back_button, edit_button;
    private TextView item_name, item_value, item_date, item_make, item_model, item_serialno, item_description, item_comment;
    private Item item;
    ChipGroup tagGroup;
    private GridView photo_view;
    private ArrayList<Bitmap> dataList;
    private ArrayAdapter<Bitmap> photoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        Intent intent = getIntent();
        back_button = findViewById(R.id.back_button);
        edit_button = findViewById(R.id.edit_button);
        tagGroup = findViewById(R.id.tagGroup);
        photo_view = findViewById(R.id.photo_view);
        item_name = findViewById(R.id.item_name);
        item_value = findViewById(R.id.item_value);
        item_date = findViewById(R.id.item_date);
        item_make = findViewById(R.id.item_make);
        item_model = findViewById(R.id.item_model);
        item_serialno = findViewById(R.id.item_serialno);
        item_description = findViewById(R.id.item_description);
        item_comment = findViewById(R.id.item_comment);
        item = intent.getParcelableExtra("item");
        changeData();

        // set tags
        ArrayList<Tag> tags = item.getTags();
        for (Tag tag: tags) {
            Chip chip = (Chip) LayoutInflater.from(DisplayActivity.this).inflate(R.layout.activity_display, null);
            chip.setText(tag.getName());
            chip.setId(tags.indexOf(tag));
            tagGroup.addView(chip);
        }

        // TODO assign values of photos from item to photos
        ArrayList<Bitmap> photos = item.getPhotos();
        photoAdapter = new ArrayAdapter<Bitmap>(this,R.layout.photos_list_item, photos);
        photo_view.setAdapter(photoAdapter);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new InputFragment(item).show(getSupportFragmentManager(), "Edit");
            }
        });

        photo_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO if clicked on photo, delete
                // TODO if clicked on last photo, call DisplayImageActivity
            }
        });
    }

    @Override
    public void onOKPressed(Item aitem) {
        assert item != null;
        this.item = aitem;
        changeData();
        Intent inte = new Intent(DisplayActivity.this, MainActivity.class);
        inte.putExtra("new_item", item);
        setResult(Activity.RESULT_OK, inte);
    }

    private void changeData() {
        item_name.setText(item.getName());
        item_value.setText(item.getValue());
        item_date.setText(item.getDate());
        item_make.setText(item.getMake());
        item_model.setText(item.getModel());
        item_serialno.setText(item.getSerialNumber());
        item_description.setText(item.getDescription());
        item_comment.setText(item.getComment());
        // TODO update photos and tags
    }
}