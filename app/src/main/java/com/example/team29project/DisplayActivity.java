package com.example.team29project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Display details of a selected inventory item
 * Allow users to view and potentially edit the item's information
 */
public class DisplayActivity extends AppCompatActivity implements InputFragment.OnFragmentInteractionListener {

    private Button back_button, edit_button;
    private TextView item_name, item_value, item_date, item_make, item_model, item_serialno, item_description, item_comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        Intent intent = getIntent();

        back_button = findViewById(R.id.back_button);
        edit_button = findViewById(R.id.edit_button);

        item_name = findViewById(R.id.item_name);
        item_value = findViewById(R.id.item_value);
        item_date = findViewById(R.id.item_date);
        item_make = findViewById(R.id.item_make);
        item_model = findViewById(R.id.item_model);
        item_serialno = findViewById(R.id.item_serialno);
        item_description = findViewById(R.id.item_description);
        item_comment = findViewById(R.id.item_comment);

        item_name.setText(intent.getStringExtra("item_name"));
        item_value.setText(intent.getStringExtra("item_value"));
        item_date.setText(intent.getStringExtra("item_date"));
        item_make.setText(intent.getStringExtra("item_make"));
        item_model.setText(intent.getStringExtra("item_model"));
        item_serialno.setText(intent.getStringExtra("item_serialno"));
        item_description.setText(intent.getStringExtra("item_description"));
        item_comment.setText(intent.getStringExtra("item_comment"));

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new InputFragment().show(getSupportFragmentManager(), "Edit");
                //TODO implement fragment
            }
        });
    }

    @Override
    public void onOKPressed(Item item) {
        // TODO change details and pass the data
    }

    //TODO implement photo functions
}