package com.example.team29project;

import java.io.Serializable;

/**
 * Represents a Tag with a name.
 */

public class Tag implements Serializable {
    private String name;

    /**
     * Constructs an instance of a tag
     * @param name name
     */
    public Tag(String name){
        this.name = name;
    }

    /**
     * Sets the name of a tag
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * gets the name of a tag
     * @return the tag name
     */
    public String getName(){
        return this.name;
    }
}
