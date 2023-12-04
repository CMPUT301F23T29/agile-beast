package com.example.team29project;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.team29project.Controller.DatabaseController;
import com.example.team29project.Model.Item;
import com.example.team29project.Model.Tag;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;

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
        itemsRef = mock(CollectionReference.class);
        tagsRef = mock(CollectionReference.class);

        Mockito.when(sb.getReference()).thenReturn(imgRef);
        Mockito.when(db.collection("Users").document(testUsername).collection("items")).thenReturn(itemsRef);
        Mockito.when(db.collection("Users").document(testUsername).collection("tags")).thenReturn(tagsRef);

        dbController = new DatabaseController("a", db, sb);
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
}
