package com.example.team29project.Controller;

import com.example.team29project.Model.Item;

import java.util.List;

/**
 * Interface callback that deals after it finishes loading Item objects from db
 */
public interface LoadItemsCallback {
    void onItemsLoaded();
    void onLoadFailure(Exception e);
}
