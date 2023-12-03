package com.example.team29project.View;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.example.team29project.R;


/**
 * creates a fragment to show user profile
 */
public class ProfileFragment extends DialogFragment {
    private TextView profile_username, profile_value;
    private final String username;
    private String value;

    /**
     * @param name String of userID
     * @param sum Total value of current Items
     */

    public ProfileFragment(String name, String sum) {
        // Required empty public constructor
        username = name;
        value = sum;
    }

    /**
     * Attaches the dialog fragment to its context.
     *
     * @param context The context to attach to.
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_profile, null);
        profile_username = view.findViewById(R.id.profile_username);
        profile_value = view.findViewById(R.id.profile_value);
        profile_username.setText("User Id: "+ username);
        String newSum = "Total Value: $";
        newSum = newSum + value;
        profile_value.setText(newSum);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);
        return builder.create();
    }
}