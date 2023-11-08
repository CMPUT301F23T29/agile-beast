package com.example.team29project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an ArrayAdapter for displaying items in a list view.
 */
public class ItemArrayAdapter extends ArrayAdapter<Item> {
    private final Context context;
    private ArrayList<Item> items;

    /**
     * Constructs a new ItemArrayAdapter with the given context and list of items.
     *
     * @param context The context in which the ItemArrayAdapter is being used.
     * @param items The list of items to be displayed.
     */
    public ItemArrayAdapter(Context context, ArrayList<Item> items) {
        super(context, 0, items);
        this.items = items;
        this.context = context;
    }
    
    /**
     * Returns the view for the item at the given position.
     *
     * @param position The position of the item.
     * @param convertView The old view to reuse, if possible.
     * @param parent The parent that this view will eventually be attached to.
     * @return The view for the item at the given position.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_layout_default,null);
        }

        Item item = items.get(position);
        TextView name = view.findViewById(R.id.item_name);
        TextView date = view.findViewById(R.id.item_date);
        TextView value = view.findViewById(R.id.item_value);

        name.setText(item.getName());
        date.setText(item.getDate());
        value.setText( item.getValue().toString());

        return view;
    }
}
