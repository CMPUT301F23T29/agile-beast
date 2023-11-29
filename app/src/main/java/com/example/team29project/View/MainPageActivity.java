package com.example.team29project.View;


import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.team29project.Controller.DatabaseController;
import com.example.team29project.Controller.FilteredItemCallback;
import com.example.team29project.Controller.LoadItemsCallback;
import com.example.team29project.Controller.LoadTagsCallback;
import com.example.team29project.Controller.SortItemCallback;
import com.example.team29project.Model.Item;
import com.example.team29project.Controller.ItemArrayAdapter;
import com.example.team29project.Model.Tag;
import com.example.team29project.R;
import com.example.team29project.Controller.TagAdapter;

import java.util.ArrayList;


/**
 * This method is called when the activity is starting.
 * It initializes the database, dataList, itemAdapter, and sets up the UI listeners.
 * @see android.app.Activity#onCreate(Bundle)
 */
public class MainPageActivity extends AppCompatActivity implements
        InputFragment.OnFragmentsInteractionListener,
        SortFragment.OnFragmentInteractionListener,
        FilterFragment.OnFragmentInteractionListener,
        LoadItemsCallback, FilteredItemCallback, SortItemCallback

{

    private TextView addItem, profile;
    private TextView editTag;
    private TextView selectBtn;
    private ItemArrayAdapter itemAdapter;
    private ListView itemsList;
    private boolean isDelete;
    private boolean isSelect;
    private TextView sumItem;
    private boolean isFilterFragmentShown = false;
    private boolean isSortFragmentShown = false;
    private ArrayList<Tag> tags;
    private ArrayList<Integer> selectedItems;
    private DatabaseController db;
    private String userId;


    /**
     * Creates a dialog with its components and listeners. Gets initial items and tags from database
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     * this contains the data it most recently supplied in onSaveInstanceState. Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Intent logInt = getIntent();
        userId = logInt.getStringExtra("userId");
        ImageButton menu = findViewById(R.id.menu);
        Button deleteButton = findViewById(R.id.delete_button);
        sumItem = findViewById(R.id.value_display);
        itemsList = findViewById(R.id.items);
        selectedItems = new ArrayList<>();
        isDelete = false;
        isSelect = false;
        // Init lists for tags and items,
        // as well as FireStore database
        db = new DatabaseController(userId);
        itemAdapter = new ItemArrayAdapter(this, db.getItems());
        itemsList.setAdapter(itemAdapter);
        db.loadInitialItems(this);
        itemsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * Handles the event click of the item
             * @param parent the parent to be used
             * @param view the view to be used
             * @param position the position to be used
             * @param id the id to be used
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0) {
                    if (isSelect) {
                        selectedItems.add(position);
                    } else if (isDelete) {
                        db.removeItem(position);
                        itemAdapter.notifyDataSetChanged();
                        isDelete = false;
                    } else {
                        Intent display = new Intent(MainPageActivity.this, ItemViewActivity.class);
                        display.putExtra("documentId", db.getItem(position).getDocId());
                        display.putExtra("userId",userId);

                        startActivity(display);
                    }

                }

            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelect) {
                    db.removeAllItems(selectedItems);
                    isSelect = false;
                    selectedItems.clear();
                    itemAdapter.notifyDataSetChanged();
                } else {
                    isDelete = true;
                }
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isDelete = false;
                popupMenu(view);
            }
        });

        Button filterButton = findViewById(R.id.filter_button);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFilterFragmentShown) {
                    isFilterFragmentShown = true;
                    new FilterFragment().show(getSupportFragmentManager(), "Filter");
                }
            }
        });

        Button sortButton = findViewById(R.id.sort_by_button);
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isSortFragmentShown) {
                    isSortFragmentShown = true;
                    new SortFragment().show(getSupportFragmentManager(), "Sort");
                }
            }
        });

    }

    /**
     * Update the summary of value of item
     */
    public void updateSum(){
        Double total =0.0;
        for(Item item : db.getItems()){
            total = total +  item.getValue();
        }

        sumItem.setText(String.valueOf(total));

    }

    /**
     * Creates the main menu popup and shows it
     * @param view the view to lay the popup over
     */
    public void popupMenu(View view) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.fragment_main_menu, null);
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, 750, height, focusable);
        popupWindow.showAtLocation(view, Gravity.LEFT, 0, 0);
        addItem = popupView.findViewById(R.id.add_new_item);
        selectBtn = popupView.findViewById(R.id.select_item);
        editTag = popupView.findViewById(R.id.edit_tag_item);
        profile = popupView.findViewById(R.id.user_profile);
        selectBtn.setOnClickListener(new View.OnClickListener() {
            /**
             * Handles the click event
             * @param v the popup menu view
             */
            @Override
            public void onClick(View v) {
                isSelect = true;
                isDelete = false;
                popupWindow.dismiss();
                selectedItems = new ArrayList<>();
            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelect = false;
                isDelete = false;
                new InputFragment(db).show(getSupportFragmentManager(), "addItems");
                popupWindow.dismiss();
            }
        });

        editTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TagDialogue(db).show(getSupportFragmentManager(), "Tags");

            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double total =0.0;
                for(Item item : db.getItems()){
                    total = total +  item.getValue();
                }
                sumItem.setText(String.valueOf(total));
                new ProfileFragment(userId, String.valueOf(total)).show(getSupportFragmentManager(), null);
            }
        });

        popupView.setOnTouchListener((v, event) -> {
            popupWindow.dismiss();
            return true;
        });
    }

    /**
     * Adds an item to the database
     *
     */

    @Override
    public void onOKPressed() {
        itemAdapter.notifyDataSetChanged();
        updateSum();
    }

    /**
     * Notifies iten adapter that its contents have changed
     *
     */


    /**
     * When cancel button pressed on InputFragment
     */
    @Override
    public void onCancelPressed() {
    }

    /**
     * Applies a filter to all of the items
     * @param filterBy the filter criteria
     * @param  data the string to match
     */
    @Override
    public void onFilterConfirmPressed(String filterBy, String data) {
        db.filter(filterBy, data, this);
    }
    /**
     * Sorts items based on some criteria
     * @param sortBy criteria to sort by
     * @param isAsc whether to reverse the order
     */
    @Override
    public void onSortConfirmPressed(String sortBy, boolean isAsc) {

        db.sort(sortBy,isAsc,this);
    }

    public void setFilterFragmentShown(boolean filterFragmentShown) {
        isFilterFragmentShown = filterFragmentShown;
    }

    public void setSortFragmentShown(boolean sortFragmentShown) {
        isSortFragmentShown = sortFragmentShown;
    }


    /**
     * Implemented methods from LoadItemsCallback
     */
    @Override
    public void onItemsLoaded() {
        itemAdapter.notifyDataSetChanged();
        updateSum();

    }

    /**
     * Implemented methods from LoadItemsCallback
     */
    @Override
    public void onLoadFailure(Exception e) {
        Toast.makeText(MainPageActivity.this, "Failed to load", Toast.LENGTH_SHORT).show();
    }

    /**
     * When filter success, updates the itemAdapter then update the total sum
     */
    @Override
    public void onFiltered() {
        itemAdapter.notifyDataSetChanged();
        updateSum();
    }

    /**
     * When it fails to filter the item, toast a message that it fails
     */

    @Override
    public void onFilteredFailure() {
        Toast.makeText(this, "Failed to filter", Toast.LENGTH_SHORT).show();

    }

    /**
     * When sort success, updates the itemAdapter
     */

    @Override
    public void onSorted() {
        itemAdapter.notifyDataSetChanged();
    }


    @Override
    public void onSortFailed() {
        Toast.makeText(this, "Failed to filter", Toast.LENGTH_SHORT).show();
    }


}

