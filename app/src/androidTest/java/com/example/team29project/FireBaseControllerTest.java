package com.example.team29project;

import static org.junit.Assert.assertEquals;

import com.example.team29project.Controller.DatabaseController;
import com.example.team29project.Model.Tag;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;

public class FireBaseControllerTest {
    private DatabaseController dbController;


    private String testUsername = "_a";

    @Before
    public void setUp() {
        FirebaseFirestore db = Mockito.mock(FirebaseFirestore.class, Mockito.RETURNS_DEEP_STUBS);
        FirebaseStorage sb = Mockito.mock(FirebaseStorage.class);

        // Set up return values for init
        StorageReference imgRef = Mockito.mock(StorageReference.class);
        CollectionReference itemsRef = Mockito.mock(CollectionReference.class);
        CollectionReference tagsRef = Mockito.mock(CollectionReference.class);

        Mockito.when(sb.getReference()).thenReturn(imgRef);
        Mockito.when(db.collection("Users").document(testUsername).collection("items")).thenReturn(itemsRef);
        Mockito.when(db.collection("Users").document(testUsername).collection("tags")).thenReturn(tagsRef);

        dbController = new DatabaseController("a", db, sb);
    }

    @Test
    public void createTagFromDocTest() {
        DocumentSnapshot doc = Mockito.mock(DocumentSnapshot.class);

        Mockito.when(doc.getId()).thenReturn("_Tag99");

        ArrayList<String> items = new ArrayList<>();
        items.add("_Item98");
        items.add("_Item97");
        Mockito.when(doc.get("items")).thenReturn(items);

        Tag createdTag = dbController.createTagFromDoc(doc);

        assertEquals(createdTag.getItems(), items);
        assertEquals(createdTag.getName(), "_Tag99");


    }
}
