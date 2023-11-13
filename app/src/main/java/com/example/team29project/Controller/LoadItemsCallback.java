package com.example.team29project.Controller;

import com.example.team29project.Model.Item;

import java.util.List;

public interface LoadItemsCallback {
    void onItemsLoaded();
    void onLoadFailure(Exception e);
}
