package com.example.team29project.Controller;

import android.net.Uri;
import android.util.Log;
import android.widget.TextView;

import com.example.team29project.Model.Item;
import com.google.android.gms.tasks.OnCompleteListener;
import androidx.annotation.Nullable;

import java.util.Arrays;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;

import java.util.Map;


public class DatabaseController  {

/**
 * Represents the database
 */

    private final FirebaseFirestore db;
    private final FirebaseStorage sb;

    // Item attributes
    private final CollectionReference itemsRef;

    private final StorageReference imageRef;
    private ArrayList<Item> itemDataList;
    // Adapters
    private ItemArrayAdapter itemAdapter;
    private TagAdapter tagAdapter;







    // Tag attributes not used for this checkpoint
    private final CollectionReference tagsRef;
    private final ArrayList<String> tagDataList;

    private static final String TAG = "DatabaseController";

    /**
     * Stores the items and tags in the database
     * images are saved on firebase Storage
     */
    public DatabaseController(String userId){
        this.db = FirebaseFirestore.getInstance();
        this.sb = FirebaseStorage.getInstance();
        this.imageRef = sb.getReference();
        this.itemsRef = db.collection("Users").document(userId).collection("items");
        this.tagsRef = db.collection("Users").document(userId).collection("tags");
        this.itemDataList = new ArrayList<>();
        this.tagDataList = new ArrayList<>();
        this.tagAdapter=null;
        this.itemAdapter = null;
    }

    /**
     *  returns the reference of images from firebase storage
     * @return
     */
    public StorageReference getImageRef(){
        return this.imageRef;
    }

    /**
     * Get a snapshot from QueryDocument then get Item object from it
     * @param doc QueryDoucmentSnapShot
     * @return item Item object
     */

    private Item createItemFromDoc(QueryDocumentSnapshot doc) {
        String name = doc.getString("name");
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
        item.setDocId(doc.getId());
        return item;
    }

    // setting ItemAdapter
    public void setAdapter(ItemArrayAdapter itemAdapter, TagAdapter tagAdapter){
        this.itemAdapter = itemAdapter;
        this.tagAdapter = tagAdapter;
    }
    // setting TagAdapter

    /**
     *
     * @param filePath Uri of the images
     * @param listener listener that initiated when it successfully upload the image
     * @param uniqueId  unique id of photo
     * @param position  position of the photo list
     */
    public void uploadPhoto(Uri filePath,OnPhotoUploadCompleteListener listener, String uniqueId,int position){
        StorageReference ref
                = imageRef
                .child(
                        "images/"
                                + uniqueId);
        ref.putFile(filePath)
                .addOnSuccessListener(
                        taskSnapshot -> listener.onPhotoUploadComplete(position))

                .addOnFailureListener(e -> {
                });

    }

    /**
     *  delete the photo with uniqueID
     * @param uniqueId id of the photos in firebase storage
     */

    public void deletePhoto(String uniqueId) {
        StorageReference photoRef = imageRef.child("images/" + uniqueId);
        // Delete the file
        photoRef.delete()
                .addOnSuccessListener(aVoid -> {
                    // File deleted successfully
                    // You can perform any additional actions here
                })
                .addOnFailureListener(exception -> {
                    // Handle any errors that occurred during the deletion
                    // For example, you might want to log the error
                    Log.e("FirebaseStorage", "Error deleting file: " + exception.getMessage());
                });
    }



    /**
     * Loads the initial tags from firebase and adds them to the tag list. Notifies the display to show the initial tags.
     */
    public void loadInitialTags() {
        // This initialize the tags from data base not used for this checkpoints
        tagsRef.addSnapshotListener((value, error) -> {
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
            }
        });
    }

    /**
     * Loads the initial items from firebase and adds them to the item list. Notifies the display to show the initial items.
     */
    public void loadInitialItems(LoadItemsCallback callback) {
        itemsRef.addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.e("Firebase", error.toString());
                if (callback != null) {
                    callback.onLoadFailure(error);
                }
            }
            if (value != null) {
                itemDataList.clear();
                for (QueryDocumentSnapshot doc: value) {
                    Item item = createItemFromDoc(doc);
                    itemDataList.add(item);
                }
            }
            if (callback != null) {
                callback.onItemsLoaded();
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
        data.put("name", item.getName());
        data.put("date", item.getDate());
        data.put("value", item.getValue());
        data.put("make", item.getMake());
        data.put("model", item.getModel());
        data.put("serialNumber", item.getSerialNumber());
        data.put("description", item.getDescription());
        data.put("comment", item.getComment());
        data.put("photos", Arrays.asList(item.getPhotos().toArray()));
        // Add the 'data' map to the Firestore database under a document named after the item's name.
        itemsRef.add(data)
                .addOnSuccessListener(documentReference -> {
                    String generatedDocumentId = documentReference.getId();
                    Log.d("Firestore", "Item added successfully with ID: " + generatedDocumentId);
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firestore", "Error adding item", e);
                    }
                });
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

                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error writing tag", e);
                });

    }

    /**
     *  Update the photos in item on firestore (edit/tag)
     * @param item item where the photos belong
     * @param photos list of strings indicating photo
     */
    public void updatePhoto(Item item,ArrayList<String> photos){
       Map<String, Object> fieldUpdate = new HashMap<>();
        fieldUpdate.put("photos", FieldValue.delete());
        itemsRef.document(item.getDocId()).update(fieldUpdate);
        for(String photo : photos) {
            itemsRef.document(item.getDocId()).update("photos", FieldValue.arrayUnion(photo));
        }
    }


    /**
     *
     * @param documentId unique string ID that represents the item
     * @param item item that has updated info
     */
    public void updateItem(String documentId , Item item){
        DocumentReference docRef = itemsRef.document(documentId);
        Map<String, Object> data = new HashMap<>();
        data.put("name", item.getName());
        data.put("date", item.getDate());
        data.put("value", item.getValue());
        data.put("make", item.getMake());
        data.put("model", item.getModel());
        data.put("serialNumber", item.getSerialNumber());
        data.put("description", item.getDescription());
        data.put("comment", item.getComment());
        docRef.update(data)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Document updated successfully!"))
                .addOnFailureListener(e -> Log.e("Firestore", "Error updating document", e));
        updatePhoto(item,item.getPhotos());

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
     *
     * @param documentId unique Id of each items
     * @param callback  indicates where the callback
     */
    public void getItem(String documentId, ItemCallback callback){
        DocumentReference docRef = itemsRef.document(documentId);
        docRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Document found, access data using documentSnapshot.getData()
                        Map<String, Object> data = documentSnapshot.getData();
                        String name = (String)data.get("name");
                        String date =  (String)data.get("date");
                        double itemValue = 0.0;
                        Object itemValueObject = data.get("value");
                        if (itemValueObject instanceof Long) {
                            itemValue = ((Long) itemValueObject).doubleValue();
                        } else if (itemValueObject instanceof Double) {
                            itemValue = (Double) itemValueObject;
                        }
                        String make = (String) data.get("make");
                        String model = (String) data.get("model");
                        String serialNumber = (String) data.get("serialNumber");
                        String description = (String) data.get("description");
                        String comment = (String) data.get("comment");
                        // Photos are stored as array
                        Object photosObject = data.get("photos");
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
                        item.setDocId(documentId);
                        // Initialize an ArrayList to store photo URLs
                        callback.onItemLoaded(item);

                        // Use the data as needed
                    } else {
                        // Document does not exist
                        Log.d("Firestore", "No such document");
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle errors
                    Log.e("Firestore", "Error getting document", e);
                    callback.onFailure(e);
                });
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
        ArrayList<String> photos =  toDeleteItem.getPhotos();
        for(String photo : photos){
            deletePhoto(photo);
        }
        itemsRef.document(toDeleteItem.getDocId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Firestore", "Item '" + toDeleteItem.getName() + "' deleted successfully");
                    }
                });
        return toDeleteItem;
    }

    /**
     * Removed all items from the list
     * @param items the items in the ArrayList
     *
     */
    public void removeAllItems(ArrayList<Integer> items) {
        for(int pos: items){
            removeItem(pos);
        }
    }

    /**
     * Filter the items based on fillter- by parameters
     * @param filterBy  the filter that is trying to be implied
     * @param data  String representations of data in firestore
     */

    public void filter(String filterBy, String data, FilteredItemCallback callback) {
         // getting data from db of items
        Query query = itemsRef;

        if(filterBy.equals("make")) {
            query = itemsRef.whereEqualTo("make", data);
        } else if (filterBy.equals("date")) {
            String[] dates = data.split(",");
            if (dates.length == 2) {
                String startDate = dates[0].trim();
                String endDate = dates[1].trim();
                query =itemsRef
                    .whereGreaterThanOrEqualTo("date", startDate)
                    .whereLessThanOrEqualTo("date", endDate);
            }
        } else if (filterBy.equals("description")) {
            final ArrayList<String> words = new ArrayList<>(Arrays.asList(data.toLowerCase().split("\\s+")));

            query =itemsRef;
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
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
        } else {
            query = itemsRef;
        }
        // Add a snapshot listener to the FireStore query
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firebase", error.toString());
                    callback.onFilteredFailure();
                }
                if (value != null) {
                    itemDataList.clear();
                    for (QueryDocumentSnapshot doc: value) {
                        Item item = createItemFromDoc(doc);
                        itemDataList.add(item);
                    }
                    callback.onFiltered();
                    //itemAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * Sort the list by sortBy
     * @param sortBy  string that is the standard of sorting
     * @param isAsc boolean value whether it is ascending or descending
     * @param callback callback for dealing after sorting
     */public void sort(String sortBy, boolean isAsc, SortItemCallback callback) {
        // sorting the data by the sortBy field in ascending or descending order
        Query.Direction direction = isAsc ? Query.Direction.ASCENDING : Query.Direction.DESCENDING;
        Query query; // New Query variable
        sortBy=sortBy.toLowerCase();
        if(sortBy.equals("default")){
            query = itemsRef;
        }
        query = itemsRef.orderBy(sortBy, direction);

        // Add a snapshot listener to the FireStore query
       query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firebase", error.toString());
                    callback.onSortFailed();
                }
                if (value != null) {
                    itemDataList.clear();
                    for (QueryDocumentSnapshot doc: value) {
                        Item item = createItemFromDoc(doc);
                        itemDataList.add(item);
                    }
                    callback.onSorted();
                }
            }
        });
    }
}
