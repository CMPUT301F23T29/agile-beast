package com.example.team29project;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Represents the database
 */
public class Database {
    private FirebaseFirestore db;

    // Item attributes
    private CollectionReference itemsRef;
    private ArrayList<Item> itemDataList;
    private ItemArrayAdapter itemAdapter;

    // Tag attributes
    private final CollectionReference tagsRef;
    private final ArrayList<String> tagDataList;
    private TagAdapter tagAdapter;

    /**
     * Stores the items and tags in the database
     */
    public Database() {

        db = FirebaseFirestore.getInstance();
        itemsRef = db.collection("items");
        tagsRef = db.collection("tags");

        this.itemDataList = new ArrayList<Item>();
        this.tagDataList = new ArrayList<String>();

    }

    /**
     * Sets the adapters
     * @param itemAdapter itemAdapter
     * @param tagAdapter tagAdapter
     */
    public void setAdapters(ItemArrayAdapter itemAdapter, TagAdapter tagAdapter) {
        this.itemAdapter = itemAdapter;
        this.tagAdapter = tagAdapter;
    }

    /**
     * Loads the initial tags from firebase and adds them to the tag list. Notifies the display to show the initial tags.
     */
    public void loadInitialTags() {
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

                    // refresh and display the new data
                    tagAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * Loads the initial items from firebase and adds them to the item list. Notifies the display to show the initial items.
     */
    public void loadInitialItems() {

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
                        // Retrieve the 'value' field as an Object
                        Object itemValueObject = doc.get("value");
                        double itemValue = 0.0;
                        // Check the type of 'itemValueObject' and convert it to a double
                        if (itemValueObject instanceof Long) {
                            itemValue = ((Long) itemValueObject).doubleValue();
                        } else if (itemValueObject instanceof Double) {
                            itemValue = (Double) itemValueObject;
                        }
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

    /**
     * Adds an item to the Firestore database
     * @param item the item being added
     */
    public void addItem(Item item) {
        // Ensure data list does not already contain item with same name
        assert (!itemDataList.contains(item));

        HashMap<String, Object> data = new HashMap<>();
        data.put("date", item.getDate());
        data.put("value", item.getValue());
        data.put("make", item.getMake());
        data.put("model", item.getModel());
        data.put("serialNumber", item.getSerialNumber());
        data.put("description", item.getDescription());
        data.put("comment", item.getComment());
        data.put("photo", item.getPhotos());
        // Add the 'data' map to the Firestore database under a document named after the item's name.
        itemsRef
                .document(item.getName())
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Firestore", "Item written successfully!");
                    }
                });

        itemAdapter.notifyDataSetChanged();
    }

    /**
     * Adds a tag to the Firestore database
     * @param tag the tag being added
     */
    public void addTag(String tag) {
        assert (!tagDataList.contains(tag));

        tagsRef.document(tag)
                        .set(tag)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d("Firestore", "Tag written successfully!");
                                    }
                                });
        tagAdapter.notifyDataSetChanged();
    }

    /**
     * Returns the itemDataList
     * @return the itemDataList containing the items
     */
    public ArrayList<Item> getItems() {
        return itemDataList;
    }

    /**
     * Returns the tagDataList
     * @return the tagDataList containing the tags
     */
    public ArrayList<String> getTags() {
        return tagDataList;
    }

    /**
     * Returns the item at provided position in the itemDataList
     * @param i the position of item in the itemDataList
     * @return item at position i
     */
    public Item getItem(int i) {
        return itemDataList.get(i);
    }

    /**
     * Removes a tag from the Firestore database
     * @param i the position of tag to be deleted in the tagDataList
     * @return the tag that was deleted
     */
    public String removeTag(int i) {
        assert (i < this.tagDataList.size());

        String toDeleteTag = this.tagDataList.get(i);
        tagsRef.document(toDeleteTag)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Firestore", "Tag '" + toDeleteTag + "' deleted successfully");
                    }
                });
        tagAdapter.notifyDataSetChanged();

        return toDeleteTag;
    }

    /**
     * Removes an item from the Firestore database
     * @param i the position of item to be deleted in the itemDataList
     * @return the item that was deleted
     */
    public Item removeItem(int i) {
        assert (i < this.itemDataList.size());

        Item toDeleteItem = this.itemDataList.get(i);

        itemsRef.document(toDeleteItem.getName())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Firestore", "Item '" + toDeleteItem.getName() + "' deleted successfully");
                    }
                });

        itemAdapter.notifyDataSetChanged();

        return toDeleteItem;
    }

    /**
     * Removed all items from the list
     * @param items the items in the ArrayList
     * @return the empty itemDataList
     */
    public boolean removeAllItems(ArrayList<Item> items) {
        return this.itemDataList.removeAll(items);
    }

    public void filter(String filterBy, String data) {
        db = FirebaseFirestore.getInstance();

        Query query; // Declare a Query object

        if(filterBy.equals("make")) {
            query = db.collection("items").whereEqualTo("make", data);
        } else if (filterBy.equals("date")) {
            //TODO date range
            query = db.collection("items");//need to change this query
        } else if (filterBy.equals("description")) {
            //TODO multiple description words or most number of words matched
            query = db.collection("items");//need to change this query
        } else {
            query = db.collection("items");
        }

        // Add a snapshot listener to the Firestore query
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                // If there's an error with the snapshot, log the error
                if (error != null) {
                    Log.e("Firebase", error.toString());
                }

                // If the snapshot is not null (i.e., there's data at 'itemsRef')
                if (value != null) {
                    // Clear the 'dataList'
                    itemDataList.clear();

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
                        itemDataList.add(new Item(name, date, (Double) itemValue, make, model, description, comment, serialNumber));
                    }

                    // refresh ListView and display the new data
                    itemAdapter.notifyDataSetChanged();
                }
            }
        });
    }


    public void sort(String sortBy, Boolean isAsc) {
        db = FirebaseFirestore.getInstance();

        // sorting the data by the sortBy field in ascending or descending order
        Query.Direction direction = isAsc ? Query.Direction.ASCENDING : Query.Direction.DESCENDING;
        Query query; // New Query variable
        sortBy=sortBy.toLowerCase();
        if (sortBy.equals("date")) {
            query = db.collection("items").orderBy("date", direction);
        } else if (sortBy.equals("value")) {
            query = db.collection("items").orderBy("value", direction);
        } else if (sortBy.equals("make")) {
            query = db.collection("items").orderBy("make", direction);
        } else if (sortBy.equals("model")) {
            query = db.collection("items").orderBy("model", direction);
        } else if (sortBy.equals("serialnumber")) {
            query = db.collection("items").orderBy("serialnumber", direction);
        } else if (sortBy.equals("description")) {
            query = db.collection("items").orderBy("description", direction);
        } else if (sortBy.equals("comment")) {
            query = db.collection("items").orderBy("comment", direction);
        } else {
            query = db.collection("items");
        }

        // Add a snapshot listener to the Firestore query
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                // If there's an error with the snapshot, log the error
                if (error != null) {
                    Log.e("Firebase", error.toString());
                }
                // If the snapshot is not null (i.e., there's data at 'itemsRef')
                if (value != null) {
                    // Clear the 'dataList'
                    itemDataList.clear();
                    //TODO figure out how to deal with null values
                    // Loop over each document in the snapshot
                    for (QueryDocumentSnapshot doc: value) {
                        // Retrieve various fields from the document
                        String name = doc.getId();
                        String date = doc.getString("date");
                        Object itemValueObject = doc.get("value");
                        double itemValue = 0.0;
                        if (itemValueObject instanceof Long) {
                            itemValue = ((Long) itemValueObject).doubleValue();
                        } else if (itemValueObject instanceof Double) {
                            itemValue = (Double) itemValueObject;
                        }
                        String make = doc.getString("make");
                        String model = doc.getString("model");
                        String serialNumber = doc.getString("serialNumber");
                        String description = doc.getString("description");
                        String comment = doc.getString("comment");

                        // Add a new 'Item' object to 'dataList' with these fields
                        itemDataList.add(new Item(name, date, itemValue, make, model, description, comment, serialNumber));
                    }

                    // refresh ListView and display the new data
                    itemAdapter.notifyDataSetChanged();
                }
            }
        });
    }




}
