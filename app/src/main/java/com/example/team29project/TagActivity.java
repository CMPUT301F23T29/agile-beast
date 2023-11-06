package com.example.team29project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class TagActivity extends AppCompatActivity implements TagDialogue.OnFragmentInteractionListener{
    private ArrayList<Tag> tags;
    private TagAdapter t_adapter;
    private Button tagAddBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);
        tags = new ArrayList<>();
        t_adapter = new TagAdapter(this, tags);
        tagAddBtn = findViewById(R.id.tagAdd);
        tagAddBtn.setOnClickListener(v ->
                //new TagDialogue(tags, t_adapter).show(getSupportFragmentManager(),"Tags"));
                new InputFragment().show(getSupportFragmentManager(), "addItems"));

    }

    @Override
    public void onAddPressed(Tag tag) {
        tags.add(tag);
        t_adapter.notifyDataSetChanged();
    }

    @Override
    public void onEditPressed() {

    }
}