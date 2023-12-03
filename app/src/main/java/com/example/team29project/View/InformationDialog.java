package com.example.team29project.View;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.example.team29project.Model.Item;
import com.example.team29project.R;

/**
 * Dialog fragment display options to search infromation from
 */
public class InformationDialog extends DialogFragment {
    Item item;

    public InformationDialog(Item item){
        this.item = item;
    }

    /**
     * creates the view with a set background colour
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.information_pick, container);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#D8E8EC")));
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.information_pick, null);
        TextView google = view.findViewById(R.id.choose_google);
        TextView amazon = view.findViewById(R.id.choose_amazon);

        String name = item.getName();
        String make = item.getMake();
        String model = item.getModel();
        // Create a link format
        String link = make+" " + name+ " " + model;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);
        builder.setTitle("Choose a place to find information");
        google.setOnClickListener(v -> {
            intent.setData(Uri.parse("https://www.google.com/search?q="+link));
            startActivity(intent);
        });
        amazon.setOnClickListener(v -> {
            intent.setData(Uri.parse("https://www.amazon.ca/s?k="+link));
            startActivity(intent);
        });
        return builder.create();
    }
}
