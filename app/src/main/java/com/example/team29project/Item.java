package com.example.team29project;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.net.URI;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Represents an item with a name, value, make, model, serial number, description, and comment.
 */
public class Item implements Serializable {
    private String name;
    private double value;
    private String description;
    private String date;
    private String make;
    private String model;
    private String serialNumber;
    private String comment;

    private ArrayList<Uri> photos;
    //private ArrayList<Tag> tags;

    public Item(
            String name , String date ,
            double value,
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
        photos = null;
    }

    public Item(
            String name , String date ,
            double value,
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
        photos = null;
    }



 /*   public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };*/
  /*  protected Item(Parcel in) {
        name = in.readString();
        value = in.readDouble();
        date = in.readString();
        model = in.readString();
        make = in.readString();
        serialNumber = in.readString();
        description = in.readString();
        comment = in.readString();

      //  in.readList(photos, Uri.class.getClassLoader());
       // in.readList(tags,Tag.class.getClassLoader());

    }*/
    public ArrayList<Uri> getPhotos(){ return photos;}

    public void setPhotos(ArrayList<Uri> photos){
        this.photos = photos;
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

   /* public void addTag (Tag tag){
        this.tags.add(tag);
    }*/
   /* public ArrayList<Tag> getTags(){
        return tags;
    }*/

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

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }



    /*@Override
    public int describeContents() {
        return 0;
    }
*/
    /*@Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeDouble(value);
        parcel.writeString(date);
        parcel.writeString(model);
        parcel.writeString(make);
        parcel.writeString(serialNumber);
        parcel.writeString(description);
        parcel.writeString(comment);
        parcel.writeTypedList();
        parcel.writeList(photos);
       // parcel.writeList(tags);
    }*/
}
