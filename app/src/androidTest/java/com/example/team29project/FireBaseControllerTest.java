package com.example.team29project;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.net.Uri;

import com.example.team29project.Controller.DatabaseController;
import com.example.team29project.Controller.LoadItemsCallback;
import com.example.team29project.Controller.OnPhotoUploadCompleteListener;
import com.example.team29project.Controller.TagModifyCallback;
import com.example.team29project.Model.Item;
import com.example.team29project.Model.Tag;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class FireBaseControllerTest {
    private DatabaseController dbController;
    private FirebaseFirestore db;
    private FirebaseStorage sb;
    private StorageReference imgRef;
    private CollectionReference itemsRef;
    private CollectionReference tagsRef;




    private final String testUsername = "_a";

    @Before
    public void setUp() {
        db = mock(FirebaseFirestore.class, Mockito.RETURNS_DEEP_STUBS);
        sb = mock(FirebaseStorage.class);

        // Set up return values for init
        imgRef = mock(StorageReference.class);
        itemsRef = mock(CollectionReference.class, Mockito.RETURNS_DEEP_STUBS);
        tagsRef = mock(CollectionReference.class);

        Mockito.when(sb.getReference()).thenReturn(imgRef);
        Mockito.when(db.collection("Users").document(testUsername).collection("items")).thenReturn(itemsRef);
        Mockito.when(db.collection("Users").document(testUsername).collection("tags")).thenReturn(tagsRef);

        dbController = new DatabaseController(testUsername, db, sb);
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


}
