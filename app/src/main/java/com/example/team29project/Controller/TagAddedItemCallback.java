package com.example.team29project.Controller;

import com.example.team29project.Model.Tag;

import java.util.ArrayList;

/**
 * Callback interface after it finishes adding tag to an item
 */
public interface TagAddedItemCallback {
    void onTagsApplied(ArrayList<Tag> tagList);
}
