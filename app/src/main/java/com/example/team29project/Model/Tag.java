package com.example.team29project.Model;

import java.util.ArrayList;

/**
 * Tag object contains the name of tag and list of items applied
 */

public class Tag {
    private String name;
    private ArrayList<String> items;

    /**
     * Construct the basic structure of Tag object
     * @param name String representation of the this Tag object
     */
    public Tag(String name) {
        this.name = name;
        this.items = new ArrayList<>();
    }

    /**
     * get the string representation of Tag
     * @return string representation of Tag
     */

    public String getName() {
        return name;
    }

    /**
     * Set the string representation of Tag
     * @param name string representation of Tag
     */

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the ArrayList of string of unique document Id of items
     * @return  ArrayList of string of unique document Id of items
     */

    public ArrayList<String> getItems() {
        return this.items;
    }
    /**
     * Set the ArrayList of string of unique document Id of items
     */

    public void setItems(ArrayList<String> items) {
        this.items = items;
    }

    /**
     * Add Item which applied this tag
     * @param itemId unique string document ID of the Item object
     */

    public void addItem(String itemId){
        this.items.add(itemId);
    }

    @Override
    public String toString() {
        return name;
    }
}
