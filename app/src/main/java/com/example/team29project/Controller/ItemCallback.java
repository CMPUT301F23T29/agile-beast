package com.example.team29project.Controller;

import com.example.team29project.Model.Item;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 *
 * Interface deals as callback from Query
 */
public interface ItemCallback {
    void onItemLoaded(Item item );
    void onFailure(Exception e);
}
