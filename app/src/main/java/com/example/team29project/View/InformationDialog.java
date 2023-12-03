package com.example.team29project.View;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
