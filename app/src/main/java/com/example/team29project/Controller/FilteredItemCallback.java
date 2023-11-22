package com.example.team29project.Controller;

import com.example.team29project.Model.Item;

/**
 * Callback interface of filterItem
 */

public interface FilteredItemCallback {
    void onFiltered();
    void onFilteredFailure();
}
