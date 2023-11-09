package com.example.team29project;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Database {
    private FirebaseFirestore db;

    // Item attributes
    private CollectionReference itemsRef;
    private ArrayList<Item> itemDataList;
    private ItemArrayAdapter itemAdapter;

    // Tag attributes
    private CollectionReference tagsRef;
    private ArrayList<String> tagDataList;

    public Database(ItemArrayAdapter itemAdapter) {
        this.itemAdapter = itemAdapter;

        db = FirebaseFirestore.getInstance();
        itemsRef = db.collection("items");
        tagsRef = db.collection("tags");

        // Load the items from FireBase remote into itemDataList and display them
        loadInitialItems();
        loadInitialTags();
    }

    private void loadInitialTags() {
        tagsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                // If there's an error with the snapshot, log the error
                if (error != null) {
                    Log.e("Firebase", error.toString());
                }

                if (value != null) {
                    // Clear the 'itemDataList'
                    tagDataList.clear();

                    // Loop over each document in the snapshot
                    for (QueryDocumentSnapshot doc : value) {
                        // Retrieve various fields from the document
                        String tag = doc.getId();

                        // Add a new 'Item' object to 'itemDataList' with these fields
                        tagDataList.add(tag);
                    }

                    // refresh ListView and display the new data
                    itemAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void loadInitialItems() {
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
                    // Clear the 'itemDataList'
                    itemDataList.clear();

                    // Loop over each document in the snapshot
                    for (QueryDocumentSnapshot doc : value) {
                        // Retrieve various fields from the document
                        String name = doc.getId();
                        String date = doc.getString("date");
                        double itemValue = Double.parseDouble(Objects.requireNonNull(doc.getString("value")));
                        String make = doc.getString("make");
                        String model = doc.getString("model");
                        String serialNumber = doc.getString("serialNumber");
                        String description = doc.getString("description");
                        String comment = doc.getString("comment");

                        // Add a new 'Item' object to 'itemDataList' with these fields
                        itemDataList.add(new Item(name, date, itemValue, make, model, description, comment, serialNumber));
                    }

                    // refresh ListView and display the new data
                    itemAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void addItem(Item item) {
        // Ensure data list does not already contain item with same name
        assert (!itemDataList.contains(item));

        itemDataList.add(item);

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

    public void addTag(String tag) {
        assert (!tagDataList.contains(tag));

        tagDataList.add(tag);
    }

    public ArrayList<Item> getItems() {
        return itemDataList;
    }

    public ArrayList<String> getTags() {
        return tagDataList;
    }

}
