package com.example.team29project.Controller;

import android.util.Log;

import com.example.team29project.Model.Item;
import com.google.android.gms.tasks.OnCompleteListener;
import androidx.annotation.Nullable;
import java.util.Arrays;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.tasks.Task;
import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;

import java.util.Map;

public class DatabaseController {

/**
 * Represents the database
 */

    private FirebaseFirestore db;

    // Item attributes
    private CollectionReference itemsRef;
    private ArrayList<Item> itemDataList;
    private ItemArrayAdapter itemAdapter;

    // Tag attributes not used for this checkpoint
    private final CollectionReference tagsRef;
    private final ArrayList<String> tagDataList;
    private TagAdapter tagAdapter;
    private static final String TAG = "DatabaseController";

    /**
     * Stores the items and tags in the database
     */
    public DatabaseController() {

 
        db = FirebaseFirestore.getInstance();
        itemsRef = db.collection("items");
        tagsRef = db.collection("tags");
        this.itemDataList = new ArrayList<Item>();
        this.tagDataList = new ArrayList<String>();

    }

    /**
     * Get a snapshot from QueryDocument then get Item object from it
     * @param doc QueryDoucmentSnapShot
     * @return item Item object
     */

    private Item createItemFromDoc(QueryDocumentSnapshot doc) {
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

        // Photos are stored as array
        Object photosObject = doc.get("photos");
        // Initialize an ArrayList to store photos
        ArrayList<String> photos = new ArrayList<>();
        if (photosObject instanceof ArrayList<?>) {
            // Convert 'photosObject' to ArrayList<String>
            for (Object photo : (ArrayList<?>) photosObject) {
                if (photo instanceof String) {
                    photos.add((String) photo);
                }
            }
        }
       Item item= new Item(name, date, itemValue, make, model, description, comment, serialNumber);
        item.setPhotos(photos);
        return item;
    }

    /**
     * Sets the item. tag adapters
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
        // This initialize the tags from data base not used for this checkpoints
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

        itemsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firebase", error.toString());
                }

                if (value != null) {
                    itemDataList.clear();
                    for (QueryDocumentSnapshot doc: value) {
                        Item item = createItemFromDoc(doc);
                        itemDataList.add(item);
                    }

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
        data.put("photos", Arrays.asList(item.getPhotos().toArray()));

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
        // Add or update the tag in the Firestore collection
        tagsRef
                .document(tag)
                .set(tag)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "Tag written successfully!");
                    tagAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error writing tag", e);
                });

    }

    /**
     *  Update the photos in item (edit/tag)
     * @param item
     * @param photos
     */
    public void updatePhoto(Item item , ArrayList<String> photos){
       Map<String, Object> fieldUpdate = new HashMap<>();
        fieldUpdate.put("photos", FieldValue.delete());
        itemsRef.document(item.getName()).update(fieldUpdate);
        for(String photo : photos) {
            itemsRef.document(item.getName()).update("photos", FieldValue.arrayUnion(photo));
        }
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

    /**
     * Filter the items based on fillter- by parameters
     * @param filterBy  the filter that is trying to be implied
     * @param data  String representations of data in firestore
     */

    public void filter(String filterBy, String data) {
        db = FirebaseFirestore.getInstance();
         // getting data from db of items
        Query query = db.collection("items");

        if(filterBy.equals("make")) {
            query = db.collection("items").whereEqualTo("make", data);
        } else if (filterBy.equals("date")) {
            String[] dates = data.split(",");
            if (dates.length == 2) {
                String startDate = dates[0].trim();
                String endDate = dates[1].trim();
                query = db.collection("items")
                    .whereGreaterThanOrEqualTo("date", startDate)
                    .whereLessThanOrEqualTo("date", endDate);
            }

        } else if (filterBy.equals("description")) {
            final ArrayList<String> words = new ArrayList<>(Arrays.asList(data.toLowerCase().split("\\s+")));

            query = db.collection("items");
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        itemDataList.clear();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String description = document.getString("description");
                            boolean containsAllWords = true;
                            for (String word : words) {
                                if (!description.toLowerCase().contains(word)) {
                                    containsAllWords = false;
                                    break;
                                }
                            }
                            if (containsAllWords) {
                                Item item = createItemFromDoc(document);
                                itemDataList.add(item);
                            }
                        }

                        itemAdapter.notifyDataSetChanged();
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
        } else {
            query = db.collection("items");// print everything
        }

        // Add a snapshot listener to the Firestore query
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firebase", error.toString());
                }

                if (value != null) {
                    itemDataList.clear();

                    for (QueryDocumentSnapshot doc: value) {
                        Item item = createItemFromDoc(doc);
                        itemDataList.add(item);

                    }

                    itemAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * Sort the list by sortBy
     * @param sortBy  string that is the standard of sorting
     * @param isAsc boolean value whether it is ascending or descending
     */

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
                if (error != null) {
                    Log.e("Firebase", error.toString());
                }

                if (value != null) {
                    itemDataList.clear();

                    for (QueryDocumentSnapshot doc: value) {
                        Item item = createItemFromDoc(doc);
                        itemDataList.add(item);
                    }

                    itemAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}