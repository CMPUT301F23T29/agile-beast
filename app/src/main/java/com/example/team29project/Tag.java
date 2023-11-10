package com.example.team29project;

import java.io.Serializable;

/**
 * Represents a Tag with a name.
 */

public class Tag implements Serializable {
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
