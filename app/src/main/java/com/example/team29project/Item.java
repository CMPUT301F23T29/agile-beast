package com.example.team29project;

public class Item {
    private String name;
    private Number value;
    private String description;
    private String date;
    private String make;
    private String model;
    private String serialNumber;
    private String comment;

    public Item(
            String name , String date ,
            Number value,
            String make, String model,
            String description, String comment,
            String serialNumber
    ){
        this.name = name;
        this.date = date;
        this.value = value;
        this.make = make;
        this.model = model;
        this.serialNumber = serialNumber;
        this.description= description;
        this.comment = comment;
    }

    public Item(
            String name , String date ,
            Number value,
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

    public Number getValue() {
        return value;
    }

    public void setValue(Number value) {
        this.value = value;
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
}
