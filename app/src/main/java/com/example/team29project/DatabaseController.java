package com.example.team29project;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import androidx.annotation.Nullable;
import java.util.Arrays;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.tasks.Task;
import java.util.ArrayList;
import java.util.HashMap;
import androidx.annotation.NonNull;
import java.util.Objects;

public class DatabaseController {
    private FirebaseFirestore db;

    // Item attributes
    private CollectionReference itemsRef;
    private ArrayList<Item> itemDataList;
    private ItemArrayAdapter itemAdapter;

    // Tag attributes
    private final CollectionReference tagsRef;
    private final ArrayList<String> tagDataList;
    private TagAdapter tagAdapter;
    private static final String TAG = "DatabaseController";

    public DatabaseController() {

        db = FirebaseFirestore.getInstance();
        itemsRef = db.collection("items");
        tagsRef = db.collection("tags");

        this.itemDataList = new ArrayList<Item>();
        this.tagDataList = new ArrayList<String>();

    }

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

        return new Item(name, date, itemValue, make, model, description, comment, serialNumber);
    }

    public void setAdapters(ItemArrayAdapter itemAdapter, TagAdapter tagAdapter) {
        this.itemAdapter = itemAdapter;
        this.tagAdapter = tagAdapter;
    }

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

    public ArrayList<Item> getItems() {
        return itemDataList;
    }

    public ArrayList<String> getTags() {
        return tagDataList;
    }

    public Item getItem(int i) {
        return itemDataList.get(i);
    }

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

    public boolean removeAllItems(ArrayList<Item> items) {
        return this.itemDataList.removeAll(items);
    }

    public void filter(String filterBy, String data) {
        db = FirebaseFirestore.getInstance();

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


    //works
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
