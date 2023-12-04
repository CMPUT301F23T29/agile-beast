package com.example.team29project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.net.Uri;

import com.example.team29project.Controller.DatabaseController;
import com.example.team29project.Controller.FilteredItemCallback;
import com.example.team29project.Controller.ItemCallback;
import com.example.team29project.Controller.LoadItemsCallback;
import com.example.team29project.Controller.OnPhotoUploadCompleteListener;
import com.example.team29project.Controller.TagModifyCallback;
import com.example.team29project.Model.Item;
import com.example.team29project.Model.Tag;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.checkerframework.checker.units.qual.A;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FireBaseControllerTest {
    private DatabaseController dbController;
    private FirebaseFirestore db;
    private FirebaseStorage sb;
    private StorageReference imgRef;
    private CollectionReference itemsRef;
    private CollectionReference tagsRef;
    private ArrayList<Item> itemDataList;
    private ArrayList<Tag> tagDataList;




    private final String testUsername = "_a";

    @Before
    public void setUp() {
        db = mock(FirebaseFirestore.class, Mockito.RETURNS_DEEP_STUBS);
        sb = mock(FirebaseStorage.class);
        itemDataList = spy(ArrayList.class);
        tagDataList = spy(ArrayList.class);

        // Set up return values for init
        imgRef = mock(StorageReference.class);
        itemsRef = mock(CollectionReference.class, Mockito.RETURNS_DEEP_STUBS);
        tagsRef = mock(CollectionReference.class, Mockito.RETURNS_DEEP_STUBS);

        Mockito.when(sb.getReference()).thenReturn(imgRef);
        Mockito.when(db.collection("Users").document(testUsername).collection("items")).thenReturn(itemsRef);
        Mockito.when(db.collection("Users").document(testUsername).collection("tags")).thenReturn(tagsRef);

        dbController = new DatabaseController(testUsername, db, sb, itemDataList, tagDataList);
    }

    @Test
    public void getImageRefTest() {
        StorageReference returnedImgRef = dbController.getImageRef();
        assertEquals(returnedImgRef, imgRef);
    }

    @Test
    public void createTagFromDocTest() {
        DocumentSnapshot doc = mock(DocumentSnapshot.class);

        when(doc.getId()).thenReturn("_Tag99");

        ArrayList<String> items = new ArrayList<>();
        items.add("_Item98");
        items.add("_Item97");
        when(doc.get("items")).thenReturn(items);

        Tag createdTag = dbController.createTagFromDoc(doc);

        assertEquals(createdTag.getItems(), items);
        assertEquals(createdTag.getName(), "_Tag99");

        verify(doc, times(1)).get("items");
    }

    @Test
    public void createItemFromDocTest() {
        DocumentSnapshot doc = mock(DocumentSnapshot.class);

        ArrayList<String> photoStrings = new ArrayList<>(Arrays.asList("_PhotoString1", "_PhotoString2", "_PhotoString3"));
        ArrayList<String> tags = new ArrayList<>(Arrays.asList("_tag1", "_tag2", "_tag3"));

        when(doc.getString("name")).thenReturn("_TestItem");
        when(doc.getString("date")).thenReturn("01/01/2000");
        when(doc.get("value")).thenReturn(101L);
        when(doc.getString("make")).thenReturn("_TestMake");
        when(doc.getString("model")).thenReturn("_TestModel");
        when(doc.getString("serialNumber")).thenReturn("_TestSerialNumber");
        when(doc.getString("description")).thenReturn("_TestDesc");
        when(doc.getString("comment")).thenReturn("_TestComment");
        when(doc.get("photos")).thenReturn(photoStrings);
        when(doc.get("tags")).thenReturn(tags);

        Item returnedItem = dbController.createItemFromDoc(doc);

        assertEquals(returnedItem.getName(), "_TestItem");
        assertEquals(returnedItem.getDate(), "01/01/2000");
        assertEquals(returnedItem.getValue(), 101, 0.001);
        assertEquals(returnedItem.getMake(), "_TestMake");
        assertEquals(returnedItem.getModel(), "_TestModel");
        assertEquals(returnedItem.getSerialNumber(), "_TestSerialNumber");
        assertEquals(returnedItem.getDescription(), "_TestDesc");
        assertEquals(returnedItem.getComment(), "_TestComment");

        assertEquals(returnedItem.getPhotos(), photoStrings);
        assertEquals(returnedItem.getTags(), tags);

    }

    @Test
    public void testPhotoTest() {
        Uri filePath = mock(Uri.class);
        OnPhotoUploadCompleteListener listener = mock(OnPhotoUploadCompleteListener.class);
        String uniqueId = "_photoId";
        int position = 3;

        StorageReference ref = mock(StorageReference.class, Mockito.RETURNS_DEEP_STUBS);

        when(imgRef.child("images/" + uniqueId)).thenReturn(ref);
        when(ref.putFile(filePath).addOnSuccessListener(taskSnapshot -> {listener.onPhotoUploadComplete(position);})).thenReturn(null);

        dbController.uploadPhoto(filePath, listener, uniqueId, position);

        verify(imgRef, times(1)).child("images/" + uniqueId);
        verify(ref, times(2)).putFile(filePath);
    }

    @Test
    public void deletePhotoTest() {
        String uniqueId = "_photoId";
        StorageReference photoRef = mock(StorageReference.class, Mockito.RETURNS_DEEP_STUBS);
//        when(photoRef.delete()).thenReturn(null);
        when(imgRef.child("images/"+uniqueId)).thenReturn(photoRef);
        when(photoRef.delete().addOnSuccessListener(aVoid -> {})).thenReturn(null);
        when(photoRef.delete().addOnFailureListener(aVoid -> {})).thenReturn(null);

        dbController.deletePhoto(uniqueId);

        verify(photoRef, times(3)).delete();
    }

    @Test
    public void loadInitialTagsTest() {
        TagModifyCallback callback = mock(TagModifyCallback.class);
        when(tagsRef.addSnapshotListener((value, error)->{})).thenReturn(null);

        dbController.loadInitialTags(callback);


        verify(tagsRef, times(1)).addSnapshotListener(ArgumentMatchers.any());
    }

    @Test
    public void loadInitialItemsTest() {
        LoadItemsCallback callback = mock(LoadItemsCallback.class);

        when(itemsRef.addSnapshotListener(ArgumentMatchers.any())).thenReturn(null);

        dbController.loadInitialItems(callback);

        verify(itemsRef, times(1)).addSnapshotListener(ArgumentMatchers.any());
    }

    @Test
    public void addItem() {
        Item item = mock(Item.class);
        String id = "_testId";
        ArrayList<String> photos = new ArrayList<>(Arrays.asList("_photo1", "_photo2"));
        ArrayList<String> tags = new ArrayList<>(Arrays.asList("_tag1", "_tag2"));

        when(item.getName()).thenReturn("_name");
        when(item.getDate()).thenReturn("_Date");
        when(item.getValue()).thenReturn(20.0);
        when(item.getMake()).thenReturn("_make");
        when(item.getModel()).thenReturn("_model");
        when(item.getSerialNumber()).thenReturn("_serial");
        when(item.getDescription()).thenReturn("_desc");
        when(item.getComment()).thenReturn("_comment");
        when(item.getPhotos()).thenReturn(photos);
        when(item.getTags()).thenReturn(tags);

        HashMap<String, Object> data = new HashMap<>();
        data.put("name", "_name");
        data.put("date", "_Date");
        data.put("value", 20.0);
        data.put("make", "_make");
        data.put("model", "_model");
        data.put("serialNumber", "_serial");
        data.put("description", "_desc");
        data.put("comment", "_comment");
        data.put("photos", photos);
        data.put("tags", tags);

        when(itemsRef.document(id).set(data).addOnSuccessListener(ArgumentMatchers.any()).addOnFailureListener(ArgumentMatchers.any())).thenReturn(null);

        dbController.addItem(item, id);

        verify(itemsRef.document(id), times(2)).set(data);
    }

    @Test
    public void addTagTest() {
        Tag tag = mock(Tag.class, Mockito.RETURNS_DEEP_STUBS);
        TagModifyCallback callback = mock(TagModifyCallback.class);
        ArrayList<String> items = new ArrayList<>(Arrays.asList("_item1", "_item2"));
        HashMap<String, Object> data = new HashMap<>();
        data.put("items", items);

        when(tag.getItems()).thenReturn(items);
        when(tag.getName()).thenReturn("_tag");
        when(tagsRef.document("_tag").set(data).addOnSuccessListener(ArgumentMatchers.any()).addOnFailureListener(any())).thenReturn(null);

        verify(tagsRef).document("_tag");
        verify(tagsRef.document("_tag")).set(data);
    }

    @Test
    public void updatePhotoTest() {
        Item item = mock(Item.class);
        ArrayList<String> photos = new ArrayList<>(Arrays.asList("_photo1", "_photo2", "_photo3"));
        Map<String, Object> fieldUpdate = new HashMap<>();
        fieldUpdate.put("photos", FieldValue.delete());

        when(itemsRef.document(item.getDocId()).update(fieldUpdate)).thenReturn(null);
        when(itemsRef.document(item.getDocId()).update(eq("photos"), FieldValue.arrayUnion(ArgumentMatchers.anyString()))).thenReturn(null);

        dbController.updatePhoto(item, photos);

        verify(itemsRef.document(item.getDocId())).update(fieldUpdate);
        verify(itemsRef.document(item.getDocId()), times(3)).update(eq("photos"), any());
    }

    @Test
    public void updateItemTest() {
        String documentId = "_docId";
        Item item = mock(Item.class);
        DocumentReference docRef = mock(DocumentReference.class, Mockito.RETURNS_DEEP_STUBS);

        ArrayList<String> photos = new ArrayList<>(Arrays.asList("_photo1", "_photo2"));
        ArrayList<String> tags = new ArrayList<>(Arrays.asList("_tag1", "_tag2"));

        when(item.getName()).thenReturn("_name");
        when(item.getDate()).thenReturn("_Date");
        when(item.getValue()).thenReturn(20.0);
        when(item.getMake()).thenReturn("_make");
        when(item.getModel()).thenReturn("_model");
        when(item.getSerialNumber()).thenReturn("_serial");
        when(item.getDescription()).thenReturn("_desc");
        when(item.getComment()).thenReturn("_comment");
        when(item.getPhotos()).thenReturn(photos);
        when(item.getTags()).thenReturn(tags);

        HashMap<String, Object> data = new HashMap<>();
        data.put("name", "_name");
        data.put("date", "_Date");
        data.put("value", 20.0);
        data.put("make", "_make");
        data.put("model", "_model");
        data.put("serialNumber", "_serial");
        data.put("description", "_desc");
        data.put("comment", "_comment");
        data.put("photos", photos);
        data.put("tags", tags);

        when(itemsRef.document(documentId)).thenReturn(docRef);
        when(docRef.update(any()).addOnSuccessListener(any()).addOnFailureListener(any())).thenReturn(null);
        when(item.getPhotos()).thenReturn(photos);

        dbController.updateItem(documentId, item);

        verify(docRef.update(data).addOnSuccessListener(any())).addOnFailureListener(any());
    }

    @Test
    public void updateTagItemTest() {
        Item item = mock(Item.class, Mockito.RETURNS_DEEP_STUBS);
        Map<String, Object> fieldUpdate = new HashMap<>();
        fieldUpdate.put("tags", FieldValue.delete());

        when(itemsRef.document(item.getDocId()).update(fieldUpdate)).thenReturn(null);
        when(item.getTags()).thenReturn(new ArrayList<>(Arrays.asList("tag1", "tag2")));

        dbController.updateTagItem(item);

        verify(itemsRef.document(item.getDocId())).update(fieldUpdate);
        verify(itemsRef.document(item.getDocId()), times(2)).update(eq("tags"), any());
    }

    @Test
    public void updateTagTest() {
        Tag tag = mock(Tag.class);
        Map<String, Object> fieldUpdate = new HashMap<>();
        fieldUpdate.put("items", FieldValue.delete());

        when(tag.getName()).thenReturn("tag1");
        when(tag.getItems()).thenReturn(new ArrayList<>(Arrays.asList("Item1", "item2")));

        dbController.updateTag(tag);

        verify(tagsRef.document("tag1")).update(fieldUpdate);
        verify(tagsRef.document("tag1"), times(2)).update(eq("items"), any());
    }

    @Test
    public void getTagsTest() {
        ArrayList<Tag> tags = dbController.getTags();

        assertNotNull(tags);
    }

    @Test
    public void getItemsTest() {
        ArrayList<Item> items = dbController.getItems();

        assertNotNull(items);
    }

    @Test
    public void getItemTest() {
        assertThrows(IndexOutOfBoundsException.class, () -> {dbController.getItem(1);});
    }

    @Test
    public void getItemByDocumentTest() {
        String documentId = "docId";
        ItemCallback callback = mock(ItemCallback.class);
        DocumentReference docRef = mock(DocumentReference.class, Mockito.RETURNS_DEEP_STUBS);

        dbController.getItem(documentId, callback);

        when(itemsRef.document(documentId)).thenReturn(docRef);
        when(docRef.get().addOnSuccessListener(documentSnapshot -> {}).addOnFailureListener(any())).thenReturn(null);

        verify(docRef).get();

    }

    @Test
    public void removeTagTest() {
        Tag tag = new Tag("tag1");
        tag.setItems(new ArrayList<>(Arrays.asList("item1", "item2")));
        tagDataList.add(tag);
        int i = 0;
        TagModifyCallback callback = mock(TagModifyCallback.class, Mockito.RETURNS_DEEP_STUBS);
        when(itemsRef.document(anyString()).update(any()).addOnSuccessListener(any()).addOnFailureListener(any())).thenReturn(null);

        dbController.removeTag(i, callback);

        verify(itemsRef.document(anyString()), times(3)).update(any());
        verify(tagsRef.document("tag1").delete()).addOnSuccessListener(any());
    }

    @Test
    public void removeItem() {
        Item item = mock(Item.class, Mockito.RETURNS_DEEP_STUBS);
        itemDataList.add(item);
        when(item.getTags()).thenReturn(new ArrayList<>(Arrays.asList("tag1", "tag2")));
        when(item.getPhotos()).thenReturn(new ArrayList<>(Arrays.asList("photo1", "photo2")));
        when(itemsRef.document(item.getDocId()).delete().addOnSuccessListener(any())).thenReturn(null);

        StorageReference photoRef = mock(StorageReference.class, Mockito.RETURNS_DEEP_STUBS);
        when(imgRef.child(anyString())).thenReturn(photoRef);
        when(photoRef.delete().addOnSuccessListener(any()).addOnFailureListener(any())).thenReturn(null);

        dbController.removeItem(0);

        verify(photoRef, times(3)).delete();
        verify(tagsRef.document("tag1")).update(any());
        verify(tagsRef.document("tag2")).update(any());
        verify(itemsRef.document(item.getDocId()), times(2)).delete();
    }

    @Test
    public void removeTagFromItemTest() {
        Item item = mock(Item.class, Mockito.RETURNS_DEEP_STUBS);
        when(item.getDocId()).thenReturn("docId");

        dbController.removeTagfromItem(item, "tag1");

        verify(tagsRef.document("tag1").update(any()).addOnSuccessListener(any())).addOnFailureListener(any());
    }

    @Test
    public void removeAllItems() {
        Item item1 = mock(Item.class);
        Item item2 = mock(Item.class);
        Item item3 = mock(Item.class);
        ArrayList<Integer> toRemove = new ArrayList<>();

        toRemove.add(0);
        toRemove.add(1);
        toRemove.add(2);

        itemDataList.add(item1);
        itemDataList.add(item2);
        itemDataList.add(item3);

        for (Item item: itemDataList) {
            when(item.getTags()).thenReturn(new ArrayList<>(Arrays.asList("tag1", "tag2")));
            when(item.getPhotos()).thenReturn(new ArrayList<>(Collections.singletonList("photo1")));
        }

        StorageReference photoRef = mock(StorageReference.class, Mockito.RETURNS_DEEP_STUBS);

        when(imgRef.child(anyString())).thenReturn(photoRef);
        when(photoRef.delete().addOnSuccessListener(any()).addOnFailureListener(any())).thenReturn(null);

        dbController.removeAllItems(toRemove);

        verify(itemsRef.document(any()), times(3)).delete();
    }

    @Test
    public void filterByMakeTest() {
        String filterBy = "make";
        String data = "apple";
        FilteredItemCallback callback = mock(FilteredItemCallback.class);
        Query query = mock(Query.class, Mockito.RETURNS_DEEP_STUBS);

        when(itemsRef.whereEqualTo("make", "apple")).thenReturn(query);
        when(query.addSnapshotListener(any())).thenReturn(null);

        dbController.filter(filterBy, data, callback);

        verify(query).addSnapshotListener(any());
        verify(itemsRef).whereEqualTo("make", "apple");
    }

    @Test
    public void filterByDescriptionTest() {
        String filterBy = "description";
        String data = "apple";
        FilteredItemCallback callback = mock(FilteredItemCallback.class);
        Query query = mock(Query.class, Mockito.RETURNS_DEEP_STUBS);

        when(itemsRef.get().addOnCompleteListener(any())).thenReturn(null);
        when(query.addSnapshotListener(any())).thenReturn(null);

        dbController.filter(filterBy, data, callback);

        verify(itemsRef.get()).addOnCompleteListener(any());
    }

    @Test
    public void filterByMakeTags() {
        String filterBy = "tags";
        String data = "apple";
        FilteredItemCallback callback = mock(FilteredItemCallback.class);
        Query query = mock(Query.class, Mockito.RETURNS_DEEP_STUBS);

        when(itemsRef.whereArrayContains("tags", "apple")).thenReturn(query);
        when(query.addSnapshotListener(any())).thenReturn(null);

        dbController.filter(filterBy, data, callback);

        verify(query).addSnapshotListener(any());
        verify(itemsRef).whereArrayContains("tags", "apple");
    }
}
