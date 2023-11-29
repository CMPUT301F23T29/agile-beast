package com.example.team29project.View;

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
import android.widget.Toast;

import androidx.annotation.ArrayRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


import com.example.team29project.Controller.DatabaseController;
import com.example.team29project.Controller.TagAddedItemCallback;
import com.example.team29project.Controller.TagModifyCallback;
import com.example.team29project.Model.Tag;
import com.example.team29project.R;
import com.example.team29project.Controller.TagAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Represents a dialog fragment for managing tags.
 */
public class TagDialogue extends DialogFragment implements TagModifyCallback  {
    private ListView tagListView;
    private ArrayList<Tag> tagList;
    private Button addTag;
    private Button deleteTag;
    private DatabaseController db;
    private TagAdapter tagAdapter;

    private TagAddedItemCallback callback;

    private boolean isPicking;


    private boolean isDelete;

    /**
     * Constructs a new TagDialogue with the given list of tags and tag adapter.
     *
     */
    public TagDialogue(DatabaseController db)
    {
        this.isPicking= false;
        this.db = db;

    }
    public TagDialogue(DatabaseController db, TagAddedItemCallback callback){
        this.isPicking=true;
        this.db = db;
        this.callback= callback;
        this.tagList= new ArrayList<>();

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
        if(isPicking){
            addTag.setVisibility(View.INVISIBLE);
            deleteTag.setVisibility(View.INVISIBLE);
        }
        tagAdapter = new TagAdapter(getContext(), db.getTags());
        tagListView.setAdapter(tagAdapter);
        EditText inputText = view.findViewById(R.id.input_tag);
        isDelete= false;
        ArrayList<Tag> tempTags = new ArrayList<>();
        db.loadInitialTags(this);
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
                         String strTag =inputText.getText().toString();
                         Tag tag = new Tag(strTag);
                         db.addTag(tag,TagDialogue.this);
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
                if(position>=0 ){
                    if(isDelete) {
                        db.removeTag(position, TagDialogue.this);
                        isDelete = false;
                    }
                    else if(isPicking){
                        if(isPicking){
                            Tag temp = tagList.get(position);
                            if(tempTags.contains(temp)){
                                tempTags.remove(temp);
                            }
                            else{
                                tempTags.add(temp);
                            }
                        }
                    }
                }
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);
        builder.setTitle("Tags");
        builder.setNegativeButton("Cancel",null);
        builder.setPositiveButton("OK", (dialog, which) -> {
            if(isPicking) {
                callback.onTagsApplied(tempTags);
            }
        });
        return builder.create();
    }

    @Override
    public void onTagModified() {
        tagAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTagsLoaded() {
        tagAdapter.notifyDataSetChanged();
        this.tagList= db.getTags();
    }

}
