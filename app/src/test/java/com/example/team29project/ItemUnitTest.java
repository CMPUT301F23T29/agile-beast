package com.example.team29project;
import org.junit.Test;

import static org.junit.Assert.*;

import com.example.team29project.Model.Item;

import java.util.ArrayList;

public class ItemUnitTest {
    @Test
    public void testItemName() throws Exception{
        Item item = new Item("name","date",1.1,"make","model","description","comment");
        assertEquals(item.getName(),"name");
    }

    @Test
    public void testItemValue() throws Exception{
        Item item = new Item("name","date",1.1,"make","model","description","comment");
        assertEquals(item.getValue(),1.1,0.001);
    }

    @Test
    public void testItemDescription() throws Exception{
        Item item = new Item("name","date",1.1,"make","model","description","comment");
        assertEquals(item.getDescription(),"description");
    }

    @Test
    public void testItemDate() throws Exception{
        Item item = new Item("name","date",1.1,"make","model","description","comment");
        assertEquals(item.getDate(),"date");
    }

    @Test
    public void testItemMake() throws Exception{
        Item item = new Item("name","date",1.1,"make","model","description","comment");
        assertEquals(item.getMake(),"make");
    }

    @Test
    public void testItemModel() throws Exception{
        Item item = new Item("name","date",1.1,"make","model","description","comment");
        assertEquals(item.getModel(),"model");
    }

    @Test
    public void testItemSerialNumber() throws Exception{
        Item item = new Item("name","date",1.1,"make","model","description","comment", "serialNumber");
        assertEquals(item.getSerialNumber(),"serialNumber");
    }

    @Test
    public void testItemComment() throws Exception{
        Item item = new Item("name","date",1.1,"make","model","description","comment");
        assertEquals(item.getComment(),"comment");
    }

    @Test
    public void testItemPhotos() throws Exception{
        Item item = new Item("name","date",1.1,"make","model","description","comment");
        ArrayList<String> photos = new ArrayList<>();
        photos.add("photo1");
        photos.add("photo2");
        item.setPhotos(photos);
        assertEquals(item.getPhotos(),photos);
    }

    @Test
    public void testItemDocId() throws Exception{
        Item item = new Item("name","date",1.1,"make","model","description","comment");
        item.setDocId("ididididid");
        String id = "ididididid";
        assertEquals(item.getDocId(), id);
    }

    @Test
    public void testItemTags() throws Exception{
        Item item = new Item("name","date",1.1,"make","model","description","comment");
        ArrayList<String> tags = new ArrayList<>();
        tags.add("tag1");
        tags.add("tag2");
        tags.add("tag3");

        ArrayList<String> tags2 = new ArrayList<>();
        tags2.add("tag1");
        tags2.add("tag2");
        tags2.add("tag3");

        item.setTags(tags);
        assertEquals(item.getTags(), tags2);
    }

    @Test
    public void testItemCompare() throws Exception{
        Item item = new Item("name","2021-01-01",1.1,"make","model","description","comment");
        Item item2 = new Item("name","2022-02-02",1.2,"make2","model2","description2","comment2");
        assertEquals(item.compareTo(item2), 0);
        item2.setName("name2");
        assertEquals(item.compareTo(item2), 1);
    }

}