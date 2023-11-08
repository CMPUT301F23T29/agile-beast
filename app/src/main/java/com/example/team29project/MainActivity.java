package com.example.team29project;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * This method is called when the activity is starting.
 * It initializes the database, dataList, itemAdapter, and sets up the UI listeners.
 * @throws NullPointerException if any findViewById operation fails.
 * @see android.app.Activity#onCreate(Bundle)
 */
public class MainActivity extends AppCompatActivity implements InputFragment.OnFragmentsInteractionListener{
    private Button camera ;
    private TextView addItem;
    private ArrayList<Item> item_list;
    private TextView editTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton menu =findViewById(R.id.menu);
//       ConstraintLayout menuBackgroundLayout = (ConstraintLayout) findViewById(R.id.menu_background_layout);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMenu(view);
            }
        });

    }

    public void popupMenu(View view) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.fragment_main_menu, null);
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, 750, height, focusable);
        popupWindow.showAtLocation(view, Gravity.LEFT, 0, 0);
        addItem = popupView.findViewById(R.id.add_new_item);
        addItem.setOnClickListener(v ->
                new InputFragment().show(getSupportFragmentManager(), "addItems")
        );
        editTag = popupView.findViewById(R.id.edit_tag_item);
        editTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        popupView.setOnTouchListener((v, event) -> {
            popupWindow.dismiss();
            return true;
        });
    }
    /**
     * This method adds a new item to the "items" collection in the Firestore database.
     * @param item The item to be added to the database.
     * @throws FirebaseFirestoreException if any Firebase Firestore operation fails.
     * @see com.google.firebase.firestore.CollectionReference#document(String)
     * @see com.google.firebase.firestore.DocumentReference#set(Object)
     */
    public void addItem(Item item) {
        HashMap<String, String> data = new HashMap<>();
        data.put("date", item.getDate());
        data.put("value", item.getValue().toString());
        data.put("make", item.getMake());
        data.put("model", item.getModel());
        data.put("serialNumber", item.getSerialNumber());
        data.put("description", item.getDescription());
        data.put("comment", item.getComment());
        // Add the 'data' map to the Firestore database under a document named after the item's name.
        itemsRef
                .document(item.getName())
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Firestore", "Document snapshot written successfully!");
                    }
                });
    }

    @Override
    public void onOKPressed(Item item) {
        //
    }

    /**
     * This method initializes the Firestore database and sets up a snapshot listener on the "items" collection.
     * The snapshot listener updates the dataList and notifies the itemAdapter whenever the data in the "items" collection changes.
     * @throws FirebaseFirestoreException if any Firebase Firestore operation fails.
     */
    private void handleDatabase() {
        db = FirebaseFirestore.getInstance();
        itemsRef = db.collection("items");

        // Add a snapshot listener to the Firestore reference 'itemsRef'
        itemsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                // If there's an error with the snapshot, log the error
                if (error != null) {
                    Log.e("Firebase", error.toString());
                }

                // If the snapshot is not null (i.e., there's data at 'itemsRef')
                if (value != null) {
                    // Clear the 'dataList'
                    dataList.clear();

                    // Loop over each document in the snapshot
                    for (QueryDocumentSnapshot doc: value) {
                        // Retrieve various fields from the document
                        String name = doc.getId();
                        String date = doc.getString("date");
                        Number itemValue = Float.parseFloat(Objects.requireNonNull(doc.getString("value")));
                        String make = doc.getString("make");
                        String model = doc.getString("model");
                        String serialNumber = doc.getString("serialNumber");
                        String description = doc.getString("description");
                        String comment = doc.getString("comment");

                        // Add a new 'Item' object to 'dataList' with these fields
                        dataList.add(new Item(name, date, itemValue, make, model, description, comment, serialNumber));
                    }

                    // refresh ListView and display the new data
                    itemAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}

