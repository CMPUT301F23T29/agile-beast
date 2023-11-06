package com.example.team29project;

/**
 * Represents a Tag with a name.
 */

public class Tag {
    private String name;

    public Tag(String name){
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}
