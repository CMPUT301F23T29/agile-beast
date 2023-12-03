package com.example.team29project.Controller;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.team29project.R;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;

/**
 * Helper class to hold an array of images
 */
public class MultiImageAdapter extends RecyclerView.Adapter<MultiImageAdapter.ViewHolder> {
    private ArrayList<String> mData;
    private Context mContext ;

    private final DatabaseController db;
    private com.example.team29project.Controller.SelectListener itemClickListener;


    /**
     * Creates an instance of MultiImageAdapter
     * @param list the list of images
     * @param context the context to be used
     * @param itemClickListener the callback for when an image is clicked
     * @param db DatabaseController object to retrieve images from database
     */
    public MultiImageAdapter(ArrayList<String> list, Context context, SelectListener itemClickListener,DatabaseController db) {
        mData = list ;
        mContext = context;
        this.itemClickListener = itemClickListener;
        this.db = db;
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

        return new ViewHolder(view);
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
            holder.image.setOnClickListener(v -> itemClickListener.onItemClick(holder.getAdapterPosition()));
        if(position ==0){
            Glide.with(mContext)
                    .load(image_uri)
                    .into(holder.image);
        }
        else {

            StorageReference dateRef = db.getImageRef().child("images/" + mData.get(position));
            dateRef.getDownloadUrl().addOnSuccessListener(downloadUrl -> Glide.with(mContext)
                    .load(downloadUrl)
                    .into(holder.image));
        }

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