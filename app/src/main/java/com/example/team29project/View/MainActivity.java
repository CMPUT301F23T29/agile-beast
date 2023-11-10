package com.example.team29project.View;

import android.content.Intent;
import android.os.Bundle;

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
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;


import com.example.team29project.Controller.DatabaseController;
import com.example.team29project.Model.Item;
import com.example.team29project.Controller.ItemArrayAdapter;
import com.example.team29project.R;
import com.example.team29project.Controller.TagAdapter;

import java.util.ArrayList;

/**
 * This method is called when the activity is starting.
 * It initializes the database, dataList, itemAdapter, and sets up the UI listeners.
 * @see android.app.Activity#onCreate(Bundle)
 */
public class MainActivity extends AppCompatActivity implements
        InputFragment.OnFragmentsInteractionListener,
        SortFragment.OnFragmentInteractionListener,
        FilterFragment.OnFragmentInteractionListener {

    private TextView addItem;
    private TextView editTag;
    private TextView selectBtn;
    private ItemArrayAdapter itemAdapter;
    private ListView itemsList;
    private int itemPosition;
    private boolean isDelete;
    private TagAdapter tagAdapter;
    private boolean isSelect;
    private TextView sumItem;
    private boolean isFilterFragmentShown = false;
    private boolean isSortFragmentShown = false;
    private ArrayList<String> tags;
    private ArrayList<Item> selectedItems;
    private DatabaseController db;
    double total ;
   ActivityResultLauncher<Intent> itemActivityResultLauncher = registerForActivityResult(
           new ActivityResultContracts.StartActivityForResult(),
           new ActivityResultCallback<ActivityResult>() {
               @Override
               public void onActivityResult(ActivityResult result) {
                   if (result.getData() !=null) {
                       Item newItem = (Item) result.getData().getExtras().getSerializable("changed_item");
                       Item temp = db.getItem(itemPosition);
                       temp.setName(newItem.getName());
                       temp.setDate(newItem.getDate());
                       temp.setValue(newItem.getValue());
                       temp.setMake(newItem.getMake());
                       temp.setModel(newItem.getModel());
                       temp.setSerialNumber(newItem.getSerialNumber());
                       temp.setDescription(newItem.getDescription());
                       temp.setComment(newItem.getComment());
                       db.updatePhoto(temp,newItem.getPhotos());
                       itemAdapter.notifyDataSetChanged();
                       updateSum();
                   }
               }
           });




    /**
     * Creates a dialog with its components and listeners. Gets initial items and tags from database
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     * this contains the data it most recently supplied in onSaveInstanceState. Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton menu = findViewById(R.id.menu);
        Button deleteButton = findViewById(R.id.delete_button);
        selectedItems = new ArrayList<Item>();
        isDelete = false;
        isSelect = false;
        sumItem = findViewById(R.id.value_display);
        // Init lists for tags and items,
        // as well as firestore database
        db = new DatabaseController();
        itemAdapter = new ItemArrayAdapter(this, db.getItems());
        tagAdapter = new TagAdapter(this,db.getTags());
        itemsList = findViewById(R.id.items);
        itemsList.setAdapter(itemAdapter);
        db.setAdapters(itemAdapter, tagAdapter);
        db.loadInitialItems();
        updateSum();

        itemAdapter.notifyDataSetChanged();
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
                        selectedItems.add(db.getItem(position));
                    } else if (isDelete) {
                        db.removeItem(position);
                        itemAdapter.notifyDataSetChanged();
                        isDelete = false;
                    } else {
                        itemPosition = position;
                        Item temp = db.getItem(position);
                        Intent display = new Intent(MainActivity.this, DisplayActivity.class);
                        display.putExtra("item", temp);
                        display.putStringArrayListExtra("tags", db.getTags());
                        itemActivityResultLauncher.launch(display);
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
//       ConstraintLayout menuBackgroundLayout = (ConstraintLayout) findViewById(R.id.menu_background_layout);
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
        total =0.0;
        for(Item item : db.getItems()){
            total = total +  item.getValue();
        }

        sumItem.setText( String.valueOf(total));

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
                selectedItems = new ArrayList<Item>();
            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelect = false;
                isDelete = false;
                new InputFragment(db.getTags()).show(getSupportFragmentManager(), "addItems");
                popupWindow.dismiss();
            }
        });
        editTag = popupView.findViewById(R.id.edit_tag_item);
        editTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  db.addTag("sss");
                //new TagDialogue(db.getTags(), tagAdapter).show(getSupportFragmentManager(), "Tags");

            }
        });
        popupView.setOnTouchListener((v, event) -> {
            popupWindow.dismiss();
            return true;
        });
    }

    /**
     * Adds an item to the database
     * @param item the item to be used
     */

    @Override
    public void onOKPressed(Item item) {

        db.addItem(item);
        updateSum();
    }

    /**
     * Notifies iten adapter that its contents have changed
     * @param item the item to be used
     */
    @Override
    public void onEditPressed(Item item) {
        itemAdapter.notifyDataSetChanged();
        updateSum();
    }


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
        db.filter(filterBy, data);
    }
    /**
     * Sorts items based on some criteria
     * @param sortBy criteria to sort by
     * @param isAsc whether to reverse the order
     */
    @Override
    public void onSortConfirmPressed(String sortBy, boolean isAsc) {
        db.sort(sortBy,isAsc);
    }

    public void setFilterFragmentShown(boolean filterFragmentShown) {
        isFilterFragmentShown = filterFragmentShown;
    }

    public void setSortFragmentShown(boolean sortFragmentShown) {
        isSortFragmentShown = sortFragmentShown;
    }

  
}
