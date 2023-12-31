package com.example.team29project.Controller;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.team29project.Model.Item;
import com.example.team29project.Model.Tag;
import java.util.Arrays;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




/**
 * This class controllers actions regarding to database.
 */

public class DatabaseController  {

    private final FirebaseFirestore db;
    private final FirebaseStorage sb;

    // Item attributes
    private final CollectionReference itemsRef;

    private final StorageReference imageRef;
    private ArrayList<Item> itemDataList;
    // Adapters

    private final CollectionReference tagsRef;
    private final ArrayList<Tag> tagDataList;

    private static final String TAG = "DatabaseController";

    /**
     * Based on the given unique ID, It initializes the database controller
     * @param userId The unique user ID indicates the user
     */
    public DatabaseController(String userId){
        this.db = FirebaseFirestore.getInstance();
        this.sb = FirebaseStorage.getInstance();
        this.imageRef = sb.getReference();
        this.itemsRef = db.collection("Users").document(userId).collection("items");
        this.tagsRef = db.collection("Users").document(userId).collection("tags");
        this.itemDataList = new ArrayList<>();
        this.tagDataList = new ArrayList<>();
    }

    public DatabaseController(String userId, FirebaseFirestore db, FirebaseStorage sb, ArrayList<Item> items, ArrayList<Tag> tags) {
        this.db = db;
        this.sb = sb;
        this.imageRef = sb.getReference();
        this.itemsRef = db.collection("Users").document(userId).collection("items");
        this.tagsRef = db.collection("Users").document(userId).collection("tags");
        this.itemDataList = items;
        this.tagDataList = tags;
    }

    /**
     *  returns the reference of images from firebase storage
     * @return StorageReference of images storage
     */
    public StorageReference getImageRef(){
        return this.imageRef;
    }

    /**
     * It creates Tag object from DoC file from fireStore
     * @param doc document file of each tag
     * @return Tag object corresponding to document file
     */

    public Tag createTagFromDoc(DocumentSnapshot doc){
        String name = doc.getId();
        Object itemsObject = doc.get("items");
        // Initialize an ArrayList to store photos
        ArrayList<String> items = new ArrayList<>();
        if (itemsObject instanceof ArrayList<?>) {
            // Convert 'photosObject' to ArrayList<String>
            for (Object item : (ArrayList<?>) itemsObject) {
                if (item instanceof String) {
                    items.add((String) item);
                }
            }
        }
        Tag tag =new Tag(name);
        tag.setItems(items);
        return tag;

    }

    /**
     * Get a snapshot from QueryDocument then get Item object from it
     * @param doc QueryDocumentSnapShot
     * @return item Item object
     */

    public Item createItemFromDoc(DocumentSnapshot doc) {
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
        Object tagsObject = doc.get("tags");
        //
        ArrayList<String> tags = new ArrayList<>();
        if (tagsObject instanceof ArrayList<?>) {
            // Convert 'tagsObject' to ArrayList<String>
            for (Object tag : (ArrayList<?>) tagsObject) {
                if (tag instanceof String) {
                    tags.add((String) tag);
                }
            }
        }
        // Sort the tags alphabetically
        Collections.sort(tags);
        Item item = new Item(name, date, itemValue, make, model, description, comment, serialNumber);
        item.setPhotos(photos);
        item.setDocId(doc.getId());
        item.setTags(tags); // Tags are now sorted alphabetically
        return item;
    }

    // setting ItemAdapter

    // setting TagAdapter

    /**
     * This uploads the images into firebase storage
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
     *  It deletes the image stored in Firebase Storage
     * @param uniqueId id of the photos in Firebase Storage
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
     * Load tags from FireStore database into the application
     * @param callback TagModifyCallback instances that is called when it finishes load the tag
     */
    public void loadInitialTags(TagModifyCallback callback) {
        // This initialize the tags from data base not used for this checkpoints
        tagsRef.addSnapshotListener((value, error) -> {
            // If there's an error with the snapshot, log the error
            if (error != null) {
                Log.e("Firebase", error.toString());
            }
            if (value != null) {
                tagDataList.clear();
                // Loop over each document in the snapshot
                for (QueryDocumentSnapshot doc : value) {
                    // Retrieve various fields from the document
                    Tag tag = createTagFromDoc(doc);
                    // Add a new 'Item' object to 'itemDataList' with these fields
                    tagDataList.add(tag);
                }
            }
            if (callback != null) {
                callback.onTagsLoaded();
            }
        });
    }

    /**
     * Loads Items from FireStore  into application
     * @param callback LoadItemsCallback instances deal when Items are loaded
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
     * Adds an new item to the FireStore database
     * @param item Item object that is being added
     * @param id String represents the unique document ID of the item in database
     */
    public void addItem(Item item,String id) {
        // Ensure data list does not already contain item with same name
        assert (!itemDataList.contains(item));
        ArrayList<String> sortedTags = new ArrayList<>(item.getTags());
        Collections.sort(sortedTags);
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
        data.put("tags", sortedTags);
        // Add the 'data' map to the FireStore database under a document named after the item's name.
        itemsRef.document(id)
                .set(data)
                .addOnSuccessListener(documentReference -> Log.d("FireStore", "Item added successfully with ID: " + id))
                .addOnFailureListener(e -> Log.e("FireStore", "Error adding item", e));
    }


    /**
     * Adds a Tag object to the FireStore database
     * @param tag the Tag object that is being added
     * @param callback the TagModifyCallback instances when it finishes add tag
     */

    public void addTag(Tag tag, TagModifyCallback callback) {
        assert (!tagDataList.contains(tag));
        // Add or update the tag in the FireStore collection tags
        HashMap<String, Object> data = new HashMap<>();
        data.put("items", Arrays.asList(tag.getItems().toArray()));
        tagsRef.document(tag.getName())
                .set(data)
                .addOnSuccessListener(documentReference -> callback.onTagModified())
                .addOnFailureListener(e -> Log.e("FireStore", "Error adding item", e));
    }

    /**
     *  Update the photos in item on FireStore (edit/tag)
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
     * This function updates the item's info in database
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
                .addOnSuccessListener(aVoid -> Log.d("FireStore", "Document updated successfully!"))
                .addOnFailureListener(e -> Log.e("FireStore", "Error updating document", e));
        updatePhoto(item,item.getPhotos());
        updateTagItem(item);

    }

    /**
     * This method updates the info on arrayList of tags(string) in to item's field on fireStore
     * @param item Item object that contains the tagList to be updated
     */
    public void updateTagItem(Item item) {
        Map<String, Object> fieldUpdate = new HashMap<>();
        fieldUpdate.put("tags", FieldValue.delete());

        // Sort the tags alphabetically before updating the document
        ArrayList<String> sortedTags = new ArrayList<>(item.getTags());
        Collections.sort(sortedTags);

        itemsRef.document(item.getDocId()).update(fieldUpdate);

        // Update the document with the sorted tags
        for (String tag : sortedTags) {
            itemsRef.document(item.getDocId()).update("tags", FieldValue.arrayUnion(tag));
        }
    }


    /**
     * update list of items that this tag applied
     * @param tag Tag object needed to be updated
     */
    public void updateTag(Tag tag){
        String tagName = tag.getName();
        Map<String, Object> fieldUpdate = new HashMap<>();
        fieldUpdate.put("items", FieldValue.delete());
        tagsRef.document(tagName).update(fieldUpdate);
        for(String item : tag.getItems()) {
            tagsRef.document(tagName).update("items", FieldValue.arrayUnion(item));
        }
    }

    public ArrayList<Item> getItems() {
        return itemDataList;
    }

    /**
     * Returns the tagDataList
     * @return the ArrayList containing the tags
     */
    public ArrayList<Tag> getTags() {
        return this.tagDataList;
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
     * @param documentId String of unique Id of each items
     * @param callback  ItemCallback instances that deals after getItem successes
     */
    public void getItem(String documentId, ItemCallback callback){
        DocumentReference docRef = itemsRef.document(documentId);
        docRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Document found, access data using documentSnapshot.getData()
                        Map<String, Object> data = documentSnapshot.getData();
                        assert data != null;
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
                        Object tagsObject = data.get("tags");
                        // Initialize an ArrayList to store photos
                        ArrayList<String> tags = new ArrayList<>();
                        if (tagsObject instanceof ArrayList<?>) {
                            // Convert 'photosObject' to ArrayList<String>
                            for (Object tag : (ArrayList<?>) tagsObject) {
                                if (tag instanceof String) {
                                    tags.add((String) tag);
                                }
                            }
                        }
                        Collections.sort(tags);
                        Item item= new Item(name, date, itemValue, make, model, description, comment, serialNumber);
                        item.setPhotos(photos);
                        item.setDocId(documentId);
                        item.setTags(tags);
                        // Initialize an ArrayList to store photo URLs
                        callback.onItemLoaded(item);

                        // Use the data as needed
                    } else {
                        // Document does not exist
                        Log.d("FireStore", "No such document");
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle errors
                    Log.e("FireStore", "Error getting document", e);
                    callback.onFailure(e);
                });
    }

    /**
     * Removes a tag from the FireStore database
     * @param i the position of tag to be deleted in the tagDataList
     * @param callback TagModifyCallback instances deals after finishes removeTag
     */
    public void removeTag(int i, TagModifyCallback callback) {
        assert (i < this.tagDataList.size());
        Tag toDeleteTag = this.tagDataList.get(i);
        for(String id: toDeleteTag.getItems()){
            Map<String, Object> updates = new HashMap<>();
            updates.put("tags", FieldValue.arrayRemove(toDeleteTag.getName()));
            itemsRef.document(id).update(updates)
                    .addOnSuccessListener(aVoid -> Log.e("FireStore", "Successfully deleted"))
                    .addOnFailureListener(e -> Log.e("FireStore", "Failed to delete"));
        }

        tagsRef.document(toDeleteTag.getName())
                .delete()
                .addOnSuccessListener(unused -> {
                    Log.d("FireStore", "Tag '" + toDeleteTag + "' deleted successfully");
                    callback.onTagModified();
                });
    }

    /**
     * Removes an item from the FireStore database
     * @param i the position of item to be deleted in the itemDataList
     */
    public void removeItem(int i) {
        assert (i < this.itemDataList.size());
        Item toDeleteItem = this.itemDataList.get(i);
        for(String tag : toDeleteItem.getTags()){
           removeTagfromItem(toDeleteItem,tag);
        }
        ArrayList<String> photos =  toDeleteItem.getPhotos();
        for(String photo : photos){
            deletePhoto(photo);
        }
        itemsRef.document(toDeleteItem.getDocId())
                .delete()
                .addOnSuccessListener(unused -> Log.d("FireStore", "Item '" + toDeleteItem.getName() + "' deleted successfully"));
    }

    /**
     * This remove a specific spring representation of tag in Item object
     * @param item Item object that needs to have updated tags
     * @param tag spring representation of tag(name) need to be deleted from item
     */
    public void removeTagfromItem(Item item , String tag){
        Map<String, Object> updates = new HashMap<>();
        updates.put("items", FieldValue.arrayRemove(item.getDocId()));
        tagsRef.document(tag).update(updates)
                .addOnSuccessListener(aVoid -> Log.e("FireStore", "Successfully deleted"))
                .addOnFailureListener(e -> Log.e("FireStore", "Failed to delete"));
    }

    /**
     * Removed all items from the  list
     * @param items ArrayList that contains the positions that need to be deleted
     */
    public void removeAllItems(ArrayList<Integer> items) {
        for(int pos: items){
            removeItem(pos);
        }
    }

    /**
     * Filter the items based on filter- by parameters
     * @param filterBy  the filter that is trying to be implied
     * @param data  String representations of data in fireStore
     * @param callback FilteredItemCallback instance deals after it is done filtering
     */

    public void filter(String filterBy, String data, FilteredItemCallback callback) {
        // getting data from db of items
        Query query = itemsRef;

        if (filterBy.equals("make")) {
            query = itemsRef.whereEqualTo("make", data);
        } else if (filterBy.equals("date")) {
            String[] dates = data.split(",");
            if (dates.length == 2) {
                String startDate = dates[0].trim();
                String endDate = dates[1].trim();
                query = itemsRef
                        .whereGreaterThanOrEqualTo("date", startDate)
                        .whereLessThanOrEqualTo("date", endDate);
            }
        } else if (filterBy.equals("description")) {
            // Fetch all documents from the Firestore collection
            itemsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        List<String> matchingIds = new ArrayList<>();
                        String[] keywords = data.split(" ");

                        // Iterate over each document
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String description = document.getString("description");

                            // Check if the description contains all keywords
                            boolean matches = true;
                            for (String keyword : keywords) {
                                assert description != null;
                                if (!description.contains(keyword)) {
                                    matches = false;
                                    break;
                                }
                            }
                            // If the description matches, add the ID to the list
                            if (matches) {
                                matchingIds.add(document.getId());
                            }
                        }
                        itemDataList.clear();

                        for (String id : matchingIds) {
                            itemsRef.document(id)
                                    .addSnapshotListener((snapshot, e) -> {
                                        if (e != null) {
                                            Log.w(TAG, "Listen failed.", e);
                                            return;
                                        }
                                        if (snapshot != null && snapshot.exists()) {
                                            // Convert the DocumentSnapshot to your model class
                                            Item item = createItemFromDoc(snapshot);
                                            // Add the item to your list
                                            itemDataList.add(item);
                                            callback.onFiltered();
                                            // Notify the adapter that the data has changed
                                        } else {
                                            Log.d(TAG, "Current data: null");
                                            callback.onFilteredFailure();
                                        }
                                    });
                        }

                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                        callback.onFilteredFailure();
                    }
                }
            });
            return;
        } else if (filterBy.equals("tags")) {
            query = itemsRef.whereArrayContains("tags", data);
        }

        query.addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.e("Firebase", error.toString());
                callback.onFilteredFailure();
            }
            if (value != null) {
                itemDataList.clear();
                for (QueryDocumentSnapshot doc : value) {
                    Item item = createItemFromDoc(doc);
                    itemDataList.add(item);
                }
                callback.onFiltered();
            }
        });
    }


    /**
     * Sort the list by sortBy
     * @param sortBy  string that is the standard of sorting
     * @param isAsc boolean value whether it is ascending or descending
     * @param callback SortItemCallback instances for dealing after sorting
     */public void sort(String sortBy, boolean isAsc, SortItemCallback callback) {
        // sorting the data by the sortBy field in ascending or descending order
        Query.Direction direction = isAsc ? Query.Direction.ASCENDING : Query.Direction.DESCENDING;
        Query query; // New Query variable
        sortBy=sortBy.toLowerCase();
        query = itemsRef.orderBy(sortBy, direction);
        if(sortBy.equals("default")){
            query = itemsRef;
        }

        // Add a snapshot listener to the FireStore query
       query.addSnapshotListener((value, error) -> {
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
       });
    }
}
