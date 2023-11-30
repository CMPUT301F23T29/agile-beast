package com.example.team29project.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.team29project.Model.Tag;
import com.example.team29project.R;

import java.util.ArrayList;
// Not used for halfway Checkpoint
/**
 * Represents an adapter for displaying tags in a list view.
 */
public class TagAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater mLayoutInflater;
    private ArrayList<Tag> tagList;

    /**
     * Constructs an instance of a tag adapter
     * @param context context of this adapter
     * @param tagList list of Tag object
     */
    public TagAdapter(Context context, ArrayList<Tag> tagList) {
        this.context = context;
        this.tagList = tagList;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    /**
     * Gets the number of tags
     * @return the number of tags
     */
    @Override
    public int getCount() {
        return tagList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.tagList.get(position);
    }

    /**
     * Gets the item id
     * @param position position
     * @return the position
     */
    @Override
    public long getItemId(int position) {
        return position;
    }


    /**
     * Creates a view
     * @param position the position to be used
     * @param converView converView
     * @param parent the parent to be used
     * @return the view
     */
    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.display_tag, null);
        TextView title =  view.findViewById(R.id.each_tag);
        title.setText(tagList.get(position).getName());
        return view;
    }

}
