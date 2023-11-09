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

public class MultiImageAdapter extends RecyclerView.Adapter<MultiImageAdapter.ViewHolder>{
    private ArrayList<String> mData;
    private Context mContext ;

    private SelectListener itemClickListener;


    MultiImageAdapter(ArrayList<String> list, Context context,SelectListener itemClickListener) {
        mData = list ;
        mContext = context;
        this.itemClickListener = itemClickListener;
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        ViewHolder(View itemView) {
            super(itemView) ;
            image = itemView.findViewById(R.id.image);
        }

    }

    @Override
    public MultiImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View view = inflater.inflate(R.layout.multi_image, parent, false) ;
        MultiImageAdapter.ViewHolder viewHolder = new MultiImageAdapter.ViewHolder(view) ;

        return viewHolder ;
    }


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


    @Override
    public int getItemCount() {
        return mData.size() ;
    }

}