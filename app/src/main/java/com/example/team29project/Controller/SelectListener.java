package com.example.team29project.Controller;

/**
 * Interface listener of which photo is being picked from RecyclerView of Images
 */
public interface SelectListener {
    /**
     * onItemClick listener
     * @param position position of the clicked object from object
     */
    void onItemClick(int position);
}
