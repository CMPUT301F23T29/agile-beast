package com.example.team29project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TagAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater mLayoutInflater;
    private ArrayList<Tag> tagList;

    public TagAdapter(Context context, ArrayList<Tag> tagList) {
        this.context = context;
        this.tagList = tagList;
        this.mLayoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return tagList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Tag getItem(int position) {
        return tagList.get(position);

    }
    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.display_tag, null);
        TextView title =  view.findViewById(R.id.each_tag);
        title.setText(tagList.get(position).getName());
        return view;
    }

}
