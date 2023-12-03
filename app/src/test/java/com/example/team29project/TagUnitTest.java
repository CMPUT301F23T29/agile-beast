package com.example.team29project;

import static org.junit.Assert.assertEquals;

import com.example.team29project.Model.Item;
import com.example.team29project.Model.Tag;

import org.junit.Test;

import java.util.ArrayList;

public class TagUnitTest {
    @Test
    public void testName() throws Exception{
        Tag tags = new Tag("test");
        assertEquals(tags.getName(),"test");
        tags.setName("test2");
        assertEquals(tags.getName(),"test2");
    }
    @Test
    public void testItems() throws Exception{
        String item = "item1";
        String item2 = "item2";
        String item3 = "item3";
        String item4 = "item4";

        ArrayList<String> items = new ArrayList<>();
        items.add(item);
        items.add(item2);
        items.add(item3);

        ArrayList<String> items2 = new ArrayList<>();
        items2.add("item1");
        items2.add("item2");
        items2.add("item3");

        Tag tags = new Tag("test");
        tags.setItems(items);
        assertEquals(tags.getItems(),items2);

        tags.addItem(item4);
        items2.add("item4");
        assertEquals(tags.getItems(),items2);
    }

    @Test
    public void testString() throws Exception{
        Tag tags = new Tag("test");
        assertEquals(tags.toString(),"test");
    }
}
