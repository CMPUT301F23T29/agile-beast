package com.example.team29project.Controller;

import com.example.team29project.Model.Item;

/**
 *
 * Interface deals as callback from Query
 */
public interface ItemCallback {
    void onItemLoaded(Item item );
    void onFailure(Exception e);
}
