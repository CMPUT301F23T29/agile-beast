package com.example.team29project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Represents an activity for managing tags.
 */
public class TagActivity extends AppCompatActivity implements TagDialogue.OnFragmentInteractionListener{
    private ArrayList<Tag> tags;
    private TagAdapter t_adapter;
    private Button tagAddBtn;

    /**
     * Initializes the tag activity, sets up the tag adapter and the add tag button.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down, 
     * this contains the data it most recently supplied in onSaveInstanceState. Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);
        tags = new ArrayList<>();
        t_adapter = new TagAdapter(this, tags);
        tagAddBtn = findViewById(R.id.tagAdd);
        tagAddBtn.setOnClickListener(v ->
                new TagDialogue(tags, t_adapter).show(getSupportFragmentManager(),"Tags"));

    }

    /**
     * Adds a new tag to the list of tags and notifies the tag adapter of the change.
     *
     * @param tag The new tag to be added.
     */
    @Override
    public void onAddPressed(Tag tag) {
        tags.add(tag);
        t_adapter.notifyDataSetChanged();
    }

    /**
     * Handles the event when the edit button is pressed.
     */
    @Override
    public void onEditPressed() {
        //TODO
    }
}