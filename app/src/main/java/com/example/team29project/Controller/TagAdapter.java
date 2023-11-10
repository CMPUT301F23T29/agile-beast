package com.example.team29project.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.team29project.R;

import java.util.ArrayList;
// Not used for halfway Checkpoint
/**
 * Represents an adapter for displaying tags in a list view.
 */
public class TagAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater mLayoutInflater;
    private ArrayList<String> tagList;

    /**
     * Constructs an instance of a tag adapter
     * @param context
     * @param tagList
     */
    public TagAdapter(Context context, ArrayList<String> tagList) {
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

    /**
     * Gets the item id
     * @param position position
     * @return theposition
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Gets the tag at a provided position
     * @param position the position of the item
     * @return the tag at the given position
     */
    @Override
    public String getItem(int position) {
        return tagList.get(position);

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
        title.setText(tagList.get(position));
        return view;
    }

}
