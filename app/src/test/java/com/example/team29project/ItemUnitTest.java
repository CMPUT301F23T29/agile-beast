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






}