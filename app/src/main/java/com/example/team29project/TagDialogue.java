package com.example.team29project;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


import java.util.ArrayList;

/**
 * Represents a dialog fragment for managing tags.
 */
public class TagDialogue extends DialogFragment {
    private ListView tagListView;
    private ArrayList<String> tagList;
    private Button addTag;
    private Button deleteTag;
    private TagAdapter tagAdapter;
    private DatabaseController db;


    private boolean isDelete;

    /**
     * Constructs a new TagDialogue with the given list of tags and tag adapter.
     *
     * @param tagList    The list of tags to be managed.
     * @param tagAdapter The adapter for displaying the tags.
     */
    public TagDialogue(ArrayList<String> tagList ,TagAdapter tagAdapter)
    {
        this.tagList = tagList;
        this.tagAdapter = tagAdapter;
        this.db = db;
    }



    /**
     * Interface for handling interactions in the TagDialogue.
     */


    /**
     * Attaches the dialog fragment to its context, ensuring that it implements OnFragmentInteractionListener.
     *
     * @param context The context to attach to.
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
    
    /**
     * Represents a dialog fragment for adding tags. The dialog includes a field for entering a tag name and a list view for 
     * displaying existing tags.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view  = LayoutInflater.from(getActivity()).inflate(R.layout.display_taglist , null);
        addTag = view.findViewById(R.id.add_tag);
        deleteTag = view.findViewById(R.id.delete_tag);
        tagListView = view.findViewById(R.id.tag_listview);
        EditText inputText = view.findViewById(R.id.input_tag);
        tagListView.setAdapter(tagAdapter);
        isDelete= false;
        addTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDelete = false;
                if (inputText.getVisibility()==View.INVISIBLE) {
                    inputText.setVisibility(View.VISIBLE);
                }
                else{
                     String tagTemp = inputText.getText().toString();
                     if (!tagTemp.isEmpty()) {
                         String tag =inputText.getText().toString();
                         tagList.add(tag);
                         //db.addTag(tag);
                         tagAdapter.notifyDataSetChanged();
                         inputText.setText("");

                     }
                     inputText.setVisibility(View.INVISIBLE);
                }
            }
        });
        deleteTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDelete = true;
            }
        });
        tagListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position>=0){
                    //tagList.remove(position);
                    tagList.remove(position);
                    //db.removeTag(position);
                    isDelete=false;
                    //tagAdapter.notifyDataSetChanged();
                }
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);
        builder.setTitle("Tags");
        builder.setNegativeButton("Done",null);
        return builder.create();

    }
}
