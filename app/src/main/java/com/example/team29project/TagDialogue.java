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

public class TagDialogue extends DialogFragment {
    private ListView tagListView;
    private ArrayList<Tag> tag_list;
    private EditText enteredTag;
    private TagAdapter tag_adapter;


    public TagDialogue(ArrayList<Tag> tagList ,TagAdapter tagAdapter)
    {
        this.tag_list = tagList;
        this.tag_adapter = tagAdapter;
    }
    private OnFragmentInteractionListener listener;
    public interface OnFragmentInteractionListener {
        void onAddPressed(Tag tag);
        void onEditPressed();
    }
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
