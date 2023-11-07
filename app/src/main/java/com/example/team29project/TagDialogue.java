package com.example.team29project;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


import java.util.ArrayList;

/**
 * Represents a dialog fragment for managing tags.
 */
public class TagDialogue extends DialogFragment {
    private ListView tagListView;
    private ArrayList<Tag> tag_list;
    private EditText enteredTag;
    private TagAdapter tag_adapter;

    /**
     * Constructs a new TagDialogue with the given list of tags and tag adapter.
     *
     * @param tagList The list of tags to be managed.
     * @param tagAdapter The adapter for displaying the tags.
     */
    public TagDialogue(ArrayList<Tag> tagList ,TagAdapter tagAdapter)
    {
        this.tag_list = tagList;
        this.tag_adapter = tagAdapter;
    }

    private OnFragmentInteractionListener listener;

    /**
     * Interface for handling interactions in the TagDialogue.
     */
    public interface OnFragmentInteractionListener {
        void onAddPressed(Tag tag);
        void onEditPressed();
    }

    /**
     * Attaches the dialog fragment to its context, ensuring that it implements OnFragmentInteractionListener.
     *
     * @param context The context to attach to.
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof OnFragmentInteractionListener){
            listener =  (OnFragmentInteractionListener) context;
        }
        else{
            throw new RuntimeException("e");
        }
    }
    
    /**
     * Represents a dialog fragment for adding tags. The dialog includes a field for entering a tag name and a list view for 
     * displaying existing tags.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view  = LayoutInflater.from(getActivity()).inflate(R.layout.display_taglist , null);
        enteredTag = view.findViewById(R.id.add_tag_edt);
        tagListView = view.findViewById(R.id.tag_listview);
        tagListView.setAdapter(tag_adapter);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);
        builder.setTitle("Add Tag");
        builder.setPositiveButton("Add", (dialog, which) -> {
            try{
                String tagName = enteredTag.getText().toString();
                if(tagName.isEmpty()){
                    throw new Exception();
                }
                listener.onAddPressed(new Tag(tagName));
            }catch( Exception e){
                Toast.makeText(getContext(), "It can't be empty!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel",null);
        return builder.create();

    }
}
