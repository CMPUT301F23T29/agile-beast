package com.example.team29project;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

/**
 * Represents an item with a name, value, make, model, serial number, description, and comment.
 */
public class Item implements Parcelable{
    private String name;
    private String value;
    private String description;
    private String date;
    private String make;
    private String model;
    private String serialNumber;
    private String comment;

    private ArrayList<Bitmap> photos;
    private ArrayList<Tag> tags;

    public Item(
            String name , String date ,
            String value,
            String make, String model,
            String description, String comment,
            String serialNumber
    ){
        this.name = name;
        this.date = date;
        this.make = make;
        this.serialNumber = serialNumber;
        this.description= description;
        this.model = model;
        this.value = value;
        this.comment = comment;
    }

    public Item(
            String name , String date ,
            String value,
            String make, String model,
            String description, String comment
    ){
        this.name = name;
        this.date = date;
        this.value = value;
        this.make = make;
        this.model = model;
        this.serialNumber = "N/A";
        this.description= description;
        this.comment = comment;
    }

    protected Item(Parcel in) {
        name = in.readString();
        description = in.readString();
        date = in.readString();
        make = in.readString();
        serialNumber = in.readString();
        model = in.readString();
        comment = in.readString();
        value = in.readString();
        photos = in.createTypedArrayList(Bitmap.CREATOR);
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ArrayList<Bitmap> getPhotos() {
        return photos;
    }

    public void addPhoto(Bitmap photo) {
        this.photos.add(photo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(value);
        parcel.writeString(date);
        parcel.writeString(model);
        parcel.writeString(make);
        parcel.writeString(serialNumber);
        parcel.writeString(description);
        parcel.writeString(comment);
        parcel.writeList(tags);
        parcel.writeList(photos);
    }
}
