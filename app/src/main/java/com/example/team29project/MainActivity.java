package com.example.team29project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SlidingDrawer;
import android.widget.TextView;

import com.google.common.util.concurrent.ListenableFuture;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Main Activity with display of items. Buttons to access the other functions of the program.
 */
public class MainActivity extends AppCompatActivity implements InputFragment.OnFragmentInteractionListener{
    private Button camera ;
    private TextView addItem;
    private TextView editTag;
    private ArrayList<Item> dataList;
    private ListView itemsList;
    private ArrayAdapter<Item> itemAdapter;
    private FirebaseFirestore db;
    private CollectionReference itemsRef;


    /**
     * This method is called when the activity is starting.
     * It initializes the database, dataList, itemAdapter, and sets up the UI listeners.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle
     * contains the data it most recently supplied in onSaveInstanceState(Bundle). Note: Otherwise it is null.
     * @throws NullPointerException if any findViewById operation fails.
     * @see android.app.Activity#onCreate(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init db, load db items into dataList and adapter
        // Initialize the database, create list for data, set up the adapter and assign to the ListView.
        handleDatabase();
        dataList = new ArrayList<>();
        itemAdapter = new ItemArrayAdapter(this, dataList);
        itemsList = findViewById(R.id.items);
        itemsList.setAdapter(itemAdapter);

        ImageButton menu = (ImageButton)findViewById(R.id.menu);
        ConstraintLayout menuBackgroundLayout = (ConstraintLayout) findViewById(R.id.menu_background_layout);
        //removed the menu code maria said to remove (the sliding right code)
        //TODO make the button do something


        Button filterButton = (Button)findViewById(R.id.filter_button);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO make fragment for filter by
            }
        });

        Button sortButton = (Button)findViewById(R.id.sort_by_button);
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO make fragment for sort by
            }
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
        //TODO this also is never called
    }

    /**
     * This method initializes the Firestore database and sets up a snapshot listener on the "items" collection.
     * The snapshot listener updates the dataList and notifies the itemAdapter whenever the data in the "items" collection changes.
     * @throws FirebaseFirestoreException if any Firebase Firestore operation fails.
     */
    private void handleDatabase() { //TODO we need to move database stuff to a different class
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
                        dataList.add(new Item(name, date, itemValue.toString(), make, model, description, comment, serialNumber));
                    }

                    // refresh ListView and display the new data
                    itemAdapter.notifyDataSetChanged();
                }
            }
        });
    }


    //not tested yet may not work
    private void sortedView(String sortBy, bool isAsc) { //TODO we need to move database stuff to a different class
        db = FirebaseFirestore.getInstance();

        // sorting the data by the sortBy field in ascending or descending order
        Query.Direction direction = isAsc ? Query.Direction.ASCENDING : Query.Direction.DESCENDING;
        if (sortBy.equals("date")) {
            itemsRef = db.collection("items").orderBy("date", direction);
        } else if (sortBy.equals("value")) {
            itemsRef = db.collection("items").orderBy("value", direction);
        } else if (sortBy.equals("make")) {
            itemsRef = db.collection("items").orderBy("make", direction);
        } else if (sortBy.equals("model")) {
            itemsRef = db.collection("items").orderBy("model", direction);
        } else if (sortBy.equals("serialNumber")) {
            itemsRef = db.collection("items").orderBy("serialNumber", direction);
        } else if (sortBy.equals("description")) {
            itemsRef = db.collection("items").orderBy("description", direction);
        } else if (sortBy.equals("comment")) {
            itemsRef = db.collection("items").orderBy("comment", direction);
        } else {
            itemsRef = db.collection("items");
        }

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
                        dataList.add(new Item(name, date, itemValue.toString(), make, model, description, comment, serialNumber));
                    }

                    // refresh ListView and display the new data
                    itemAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    //not tested yet may not work
    private void filteredView(String filterBy, String value) { //TODO we need to move database stuff to a different class
        db = FirebaseFirestore.getInstance();

        // filtering the data by the sortBy field in ascending or descending order
        if(filterBy.equals("make"))
            itemsRef = db.collection("items").whereEqualTo("make", value);
        } else if (filterBy.equals("date")){
            //TODO date range
        } else if (filterBy.equals("description")){
            //TODO multiple description words or most number of words matched
        } else {
            itemsRef = db.collection("items");
        }

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
                        dataList.add(new Item(name, date, itemValue.toString(), make, model, description, comment, serialNumber));
                    }

                    // refresh ListView and display the new data
                    itemAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}

