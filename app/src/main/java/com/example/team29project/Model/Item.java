package com.example.team29project.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents an item with a name, value, make, model, serial number, description, and comment.
 */
public class Item implements Serializable, Comparable {
    private String name;
    private double value;
    private String description;
    private String date;
    private String make;
    private String model;
    private String serialNumber;
    private String comment;

    private ArrayList<String> photos;
    private ArrayList<String> tags;
    private String docId ;

    /**
     * Constructs an instance of item
     * @param name the name to be used
     * @param date the date to be used
     * @param value the value to be used
     * @param make the make to be used
     * @param model the model to be used
     * @param description the description to be used
     * @param comment the comment to be used
     * @param serialNumber the serial number to be used
     */
    public Item(
            String name, String date,
            double value,
            String make, String model,
            String description, String comment,
            String serialNumber
    ) {
        this.name = name;
        this.date = date;
        this.make = make;
        this.serialNumber = serialNumber;
        this.description = description;
        this.model = model;
        this.value = value;
        this.comment = comment;
        this.photos = new ArrayList<>();
        this.tags = new ArrayList<>();
    }

    /**
     * Constructs an instance of item
     * @param name the name to be used
     * @param date the date to be used
     * @param value the value to be used
     * @param make the make to be used
     * @param model the model to be used
     * @param description the description to be used
     * @param comment the comment to be used
     *
     */
    public Item(
            String name, String date,
            double value,
            String make, String model,
            String description, String comment
    ) {
        this.name = name;
        this.date = date;
        this.value = value;
        this.make = make;
        this.model = model;
        this.serialNumber = "N/A";
        this.description = description;
        this.comment = comment;
        this.photos = new ArrayList<>();
        this.tags = new ArrayList<>();
    }




    /**
     * Photos represents array of string indicating unique IID stored in firebase storage
     * @return the photos string
     */
    public ArrayList<String> getPhotos() {
        return photos;
    }

    /**
     * Sets the photos string
     * @param photos photos
     */
    public void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
    }

    /**
     * Gets the name of the item
     * @return the name of the item
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the item
     * @param name the name of the item
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description of the item
     * @return description of the item
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of an item
     * @param description item description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the date of an item
     * @return the date of an item
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the date of an item
     * @param date date of the item
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Gets the make of an item
     * @return the make of an item
     */
    public String getMake() {
        return make;
    }

    /**
     * Sets the make of an item
     * @param make make of an item
     */
    public void setMake(String make) {
        this.make = make;
    }

    /**
     * Gets the serial number of an item
     * @return serial number of an item
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * Sets the serial number of an item
     * @param serialNumber serial number of an item
     */
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    /**
     * Gets the model of the item
     * @return item model
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets the model of an item
     * @param model item model
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Gets the comment for an item
     * @return an item comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the comment of an item
     * @param comment item comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Gets the value of an item
     * @return item value
     */
    public Double getValue() {
        return value;
    }

    /**
     * Sets the value of an item
     * @param value item value
     */
    public void setValue(Double value) {
        this.value = value;
    }

    /**
     * Set uniquesDocument reference of the item in db
     * @param id String of uniques document Id of this Item object
     */
    public void setDocId(String id){
        this.docId =id;
    }

    /**
     * Return the String object
     * @return String unique Id of this Item object
     */
    public  String getDocId(){
        return this.docId;
    }

    /**
     * Returns the ArrayList of String representation of Tag object that is applied on to this Item object
     * @return
     */
    public ArrayList<String> getTags(){
        return this.tags;
    }

    /**
     * Set the ArrayList of String representation of  Tag object that is applied on to this Item object
     * @param tags
     */
    public void setTags(ArrayList<String> tags){
        this.tags = tags;
    }


    /**
     * Compares the item
     * @param item item
     * @return item compared
     */
    @Override
    public int compareTo(Object item) {
        return ((Item) item).name.compareTo(this.name);
    }
}


