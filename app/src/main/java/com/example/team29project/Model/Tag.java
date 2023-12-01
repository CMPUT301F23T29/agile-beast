package com.example.team29project.Model;

import java.util.ArrayList;

/**
 * Tag object contains the name of tag and list of items applied
 */

public class Tag {
    private String name;
    private ArrayList<String> items;
    public Tag(String name) {
        this.name = name;
        this.items = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getItems() {
        return this.items;
    }

    public void setItems(ArrayList<String> items) {
        this.items = items;
    }

    /**
     * Add Item which applied this tag
     * @param itemId
     */

    public void addItem(String itemId){
        this.items.add(itemId);
    }

    @Override
    public String toString() {
        return name;
    }
}
