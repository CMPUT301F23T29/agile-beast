package com.example.team29project;

import android.content.Intent;
import android.os.Bundle;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import androidx.fragment.app.Fragment;


import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import java.util.ArrayList;
import java.util.Objects;

/**
 * This method is called when the activity is starting.
 * It initializes the database, dataList, itemAdapter, and sets up the UI listeners.
 * @throws NullPointerException if any findViewById operation fails.
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
    private boolean isFilterFragmentShown = false;
    private boolean isSortFragmentShown = false;
    private ArrayList<String> tags;
    private ArrayList<Item> selectedItems;
    private DatabaseController db;
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

        // Init lists for tags and items,
        // as well as firestore database
        db = new DatabaseController();

        itemAdapter = new ItemArrayAdapter(this, db.getItems());
        tagAdapter = new TagAdapter(this,db.getTags());
        itemsList = findViewById(R.id.items);
        itemsList.setAdapter(itemAdapter);

        db.setAdapters(itemAdapter, tagAdapter);
        db.loadInitialItems();

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
                new TagDialogue(db.getTags(), tagAdapter).show(getSupportFragmentManager(), "Tags");

            }
        });
        popupView.setOnTouchListener((v, event) -> {
            popupWindow.dismiss();
            return true;
        });
    }
    /*  /**
     * This method adds a new item to the "items" collection in the Firestore database.
     * @param item The item to be added to the database.
     * @throws FirebaseFirestoreException if any Firebase Firestore operation fails.
     * @see com.google.firebase.firestore.CollectionReference#document(String)
     * @see com.google.firebase.firestore.DocumentReference#set(Object)
     */
  /*  public void addItem(Item item) {
        HashMap<String, String> data = new HashMap<>();
        data.put("date", item.getDate());
        data.put("value", item.getValue().toString());
        data.put("make", item.getMake());
        data.put("model", item.getModel());
        data.put("serialNumber", item.getSerialNumber());
        data.put("description", item.getDescription());
        data.put("comment", item.getComment());
        // Add the 'data' map to the Firestore database under a document named after the item's name.
      /*  itemsRef
                .document(item.getName())
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Firestore", "Document snapshot written successfully!");
                    }
                });
    }*/

    /**
     * Adds an item to the database
     * @param item the item to be used
     */

    @Override
    public void onOKPressed(Item item) {
        db.addItem(item);
    }

    /**
     * Notifies iten adapter that its contents have changed
     * @param item the item to be used
     */
    @Override
    public void onEditPressed(Item item) {
        itemAdapter.notifyDataSetChanged();
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
    public void onSortConfirmPressed(String sortBy, Boolean isAsc) {
        db.sort(sortBy,isAsc);
    }

    public void setFilterFragmentShown(boolean filterFragmentShown) {
        isFilterFragmentShown = filterFragmentShown;
    }

    public void setSortFragmentShown(boolean sortFragmentShown) {
        isSortFragmentShown = sortFragmentShown;
    }

  
}

