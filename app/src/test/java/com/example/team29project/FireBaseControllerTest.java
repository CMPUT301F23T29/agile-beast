package com.example.team29project;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.team29project.Controller.DatabaseController;
import com.example.team29project.Model.Tag;
import com.google.firebase.firestore.DocumentSnapshot;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class FireBaseControllerTest {
    private DatabaseController db = new DatabaseController("a");

    @Test
    public void createTagFromDocTest() {
        DocumentSnapshot doc = mock(DocumentSnapshot.class);

        when(doc.getId()).thenReturn("_Tag99");

        ArrayList<String> items = new ArrayList<>();
        items.add("_Item98");
        items.add("_Item97");
        when(doc.get("Items")).thenReturn(items);

        Tag createdTag = db.createTagFromDoc(doc);

        assertEquals(createdTag.getItems(), items);
        assertEquals(createdTag.getName(), "_Tag99");


    }
}
