package com.example.team29project.Controller;

import com.example.team29project.Model.Item;

/**
 * Interface callback deals with after Action on ItemObject from database
 */
public interface ItemCallback {
    void onItemLoaded(Item item );
    void onFailure(Exception e);
}
