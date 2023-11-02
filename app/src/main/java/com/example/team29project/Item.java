package com.example.team29project;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Item {
    private String name;
    private String description;
    private String date;
    private String make;
    private String serialNumber;

    private ArrayList<Bitmap> photos;
    private ArrayList<Tag> tags;

    public Item(String name , String date , String make, String serialNumber, String description){
        this.name = name;
        this.date = date;
        this.make = make;
        this.serialNumber = serialNumber;
        this.description= description;
    }
    public Item(String name, String date, String make, String serialNumber){
        this.name = name;
        this.date = date;
        this.make = make;
        this.serialNumber = serialNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void addTag (Tag tag){
        this.tags.add(tag);
    }
    public ArrayList<Tag> getTags(){
        return tags;
    }

}
