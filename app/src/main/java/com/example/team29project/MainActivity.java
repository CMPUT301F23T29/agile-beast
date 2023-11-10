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
import java.util.ArrayList;
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


    private ArrayList<Item> selectedItems;

    private Database db;
    ActivityResultLauncher<Intent> itemActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // on Edit
                    if (result.getData() != null) {
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
                        db.updatePhoto(newItem, newItem.getPhotos());
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

        db = new Database();

        itemAdapter = new ItemArrayAdapter(this, db.getItems());
        tagAdapter = new TagAdapter(this, db.getTags());
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

        Button filterButton = (Button) findViewById(R.id.filter_button);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FilterFragment().show(getSupportFragmentManager(), "Filter");
            }
        });

        Button sortButton = (Button) findViewById(R.id.sort_by_button);
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SortFragment().show(getSupportFragmentManager(), "Sort");
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
     *
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

            db = FirebaseFirestore.getInstance();

            Query query; // Declare a Query object

            if(filterBy.equals("make")) {
                query = db.collection("items").whereEqualTo("make", data);
            } else if (filterBy.equals("date")) {
                //TODO date range
                query = db.collection("items");//need to change this query
            } else if (filterBy.equals("description")) {
                //TODO multiple description words or most number of words matched
                query = db.collection("items");//need to change this query
            } else {
                query = db.collection("items");
            }

            // Add a snapshot listener to the Firestore query
            query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                /**
                 * Handles a Firebase event
                 * @param  value value to be used
                 * @param error any error that occurs
                 */
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    // If there's an error with the snapshot, log the error
                    if (error != null) {
                        Log.e("Firebase", error.toString());
                    }

                    // If the snapshot is not null (i.e., there's data at 'itemsRef')
                    if (value != null) {
                        // Clear the 'dataList'
                        dataList.clear();

                        // Loop over each document in the snapshot
                        for (QueryDocumentSnapshot doc: value) {
                            // Retrieve various fields from the document
                            String name = doc.getId();
                            String date = doc.getString("date");
                            Number itemValue = Float.parseFloat(Objects.requireNonNull(doc.getString("value")));

                            String make = doc.getString("make");
                            String model = doc.getString("model");
                            String serialNumber = doc.getString("serialNumber");
                            String description = doc.getString("description");
                            String comment = doc.getString("comment");

                            // Add a new 'Item' object to 'dataList' with these fields
                            dataList.add(new Item(name, date, (Double) itemValue, make, model, description, comment, serialNumber));
                        }

                        // refresh ListView and display the new data
                        itemAdapter.notifyDataSetChanged();
                    }
                }
            });

            db.filter(filterBy, data);

        }



    /**
     * Sorts items based on some criteria
     * @param sortBy criteria to sort by
     * @param isAsc whether to reverse the order
     */
    @Override
    public void onSortConfirmPressed(String sortBy, Boolean isAsc) {


        db = FirebaseFirestore.getInstance();

        // sorting the data by the sortBy field in ascending or descending order
        Query.Direction direction = isAsc ? Query.Direction.ASCENDING : Query.Direction.DESCENDING;
        Query query; // New Query variable
        sortBy=sortBy.toLowerCase();
        if (sortBy.equals("date")) {
            query = db.collection("items").orderBy("date", direction);
        } else if (sortBy.equals("value")) {
            query = db.collection("items").orderBy("value", direction);
        } else if (sortBy.equals("make")) {
            query = db.collection("items").orderBy("make", direction);
        } else if (sortBy.equals("model")) {
            query = db.collection("items").orderBy("model", direction);
        } else if (sortBy.equals("serialnumber")) {
            query = db.collection("items").orderBy("serialnumber", direction);
        } else if (sortBy.equals("description")) {
            query = db.collection("items").orderBy("description", direction);
        } else if (sortBy.equals("comment")) {
            query = db.collection("items").orderBy("comment", direction);
        } else {
            query = db.collection("items");
        }

        // Add a snapshot listener to the Firestore query
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            /**
             * Handles firebase event
             * @param value to be used
             * @param error any error that occurs
             */
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                // If there's an error with the snapshot, log the error
                if (error != null) {
                    Log.e("Firebase", error.toString());
                }

                // If the snapshot is not null (i.e., there's data at 'itemsRef')
                if (value != null) {
                    // Clear the 'dataList'
                    dataList.clear();

                    //TODO figure out how to deal with null values


                    // Loop over each document in the snapshot
                    for (QueryDocumentSnapshot doc: value) {
                        // Retrieve various fields from the document
                        String name = doc.getId();
                        String date = doc.getString("date");
                        Number itemValue = Float.parseFloat(Objects.requireNonNull(doc.getString("value")));
                        String make = doc.getString("make");
                        String model = doc.getString("model");
                        String serialNumber = doc.getString("serialNumber");
                        String description = doc.getString("description");
                        String comment = doc.getString("comment");

                        // Add a new 'Item' object to 'dataList' with these fields
                        dataList.add(new Item(name, date, (Double) itemValue, make, model, description, comment, serialNumber));
                    }

                    // refresh ListView and display the new data
                    itemAdapter.notifyDataSetChanged();
                }
            }
        });
        db.sort(sortBy,isAsc);

   
    }


}

