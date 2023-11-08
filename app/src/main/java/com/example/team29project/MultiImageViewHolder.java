package com.example.team29project;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class  MultiImageViewHolder extends RecyclerView.ViewHolder {
    // Define views that you want to make clickable
    public TextView itemTextView;

    public MultiImageViewHolder(View itemView) {
        super(itemView);

        // Initialize your views
        itemTextView = itemView.findViewById(R.id.image);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();

            }
        });
    }
}
