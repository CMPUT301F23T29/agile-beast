package com.example.team29project;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to hold an array of images
 */
public class MultiImageAdapter extends RecyclerView.Adapter<MultiImageAdapter.ViewHolder>{
    private ArrayList<String> mData;
    private Context mContext ;

    private com.example.team29project.Controller.SelectListener itemClickListener;


    /**
     * Creates an instance of MultiImageAdapter
     * @param list the list of images
     * @param context the context to be used
     * @param itemClickListener the callback for when an image is clicked
     */
    MultiImageAdapter(ArrayList<String> list, Context context, com.example.team29project.Controller.SelectListener itemClickListener) {
        mData = list ;
        mContext = context;
        this.itemClickListener = itemClickListener;
    }


    /**
     * Helper class to hold an image view
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        ViewHolder(View itemView) {
            super(itemView) ;
            image = itemView.findViewById(R.id.image);
        }

    }

    /**
     * Create a view holder associated with a view
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     * an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return the view holder
     */
    @Override
    public MultiImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View view = inflater.inflate(R.layout.multi_image, parent, false) ;
        MultiImageAdapter.ViewHolder viewHolder = new MultiImageAdapter.ViewHolder(view) ;

        return viewHolder ;
    }


    /**
     * Callback for when a view holder is bound to a view
     * @param holder The ViewHolder which should be updated to represent the contents of the
     * item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(MultiImageAdapter.ViewHolder holder, int position) {
            Uri image_uri = Uri.parse(mData.get(position));
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    itemClickListener.onItemClick(holder.getAdapterPosition());
                }
            });

            Glide.with(mContext)
                    .load(image_uri)
                    .into(holder.image);

    }


    /**
     * Gets the number of items
     * @return the number of items
     */
    @Override
    public int getItemCount() {
        return mData.size() ;
    }

}