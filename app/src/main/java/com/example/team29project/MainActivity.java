package com.example.team29project;

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

import java.util.ArrayList;

/**
 * This method is called when the activity is starting.
 * It initializes the database, dataList, itemAdapter, and sets up the UI listeners.
 * @throws NullPointerException if any findViewById operation fails.
 * @see android.app.Activity#onCreate(Bundle)
 */
public class MainActivity extends AppCompatActivity implements InputFragment.OnFragmentsInteractionListener{

    private TextView addItem;
    private TextView editTag;
    private TextView selectBtn;
    private ItemArrayAdapter itemAdapter;
    private ListView itemsList;
    private int itemPosition ;
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
                       temp.setPhotos(newItem.getPhotos());
                       itemAdapter.notifyDataSetChanged();
                   }
               }
           });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton menu =findViewById(R.id.menu);
        Button deleteButton = findViewById(R.id.delete_button);
        selectedItems = new ArrayList<Item>();
        isDelete= false;
        isSelect= false;

        // Init lists for tags and items,
        // as well as firestore database
        db = new Database();

        itemAdapter = new ItemArrayAdapter(this, db.getItems());
        tagAdapter = new TagAdapter(this, db.getTags());
        itemsList = findViewById(R.id.items);
        itemsList.setAdapter(itemAdapter);

        db.setAdapters(itemAdapter, tagAdapter);
        db.loadInitialItems();

        itemAdapter.notifyDataSetChanged();
        itemsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position >=0) {
                    if(isSelect){
                        selectedItems.add(db.getItem(position));
                    }

                    else if(isDelete){
                        db.removeItem(position);
                        itemAdapter.notifyDataSetChanged();
                        isDelete= false;
                    }
                    else {
                        itemPosition = position;
                        Item temp = db.getItem(position);
                        Intent display = new Intent(MainActivity.this, DisplayActivity.class);
                        display.putExtra("item", temp);
                        display.putStringArrayListExtra("tags",db.getTags());
                        itemActivityResultLauncher.launch(display);
                    }

                }

            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSelect){
                    db.removeAllItems(selectedItems);
                    isSelect= false;
                    selectedItems.clear();
                    itemAdapter.notifyDataSetChanged();
                }
                else{
                    isDelete = true;
                }
            }
        });
//       ConstraintLayout menuBackgroundLayout = (ConstraintLayout) findViewById(R.id.menu_background_layout);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isDelete=false;
                popupMenu(view);
            }
        });

    }

    public void popupMenu(View view) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.fragment_main_menu, null);
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, 750, height, focusable);
        popupWindow.showAtLocation(view, Gravity.LEFT, 0, 0);
        addItem = popupView.findViewById(R.id.add_new_item);
        selectBtn= popupView.findViewById(R.id.select_item);
        selectBtn.setOnClickListener(new View.OnClickListener() {
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
                isSelect =false;
                isDelete =false;
                new InputFragment(db.getTags()).show(getSupportFragmentManager(), "addItems");
                popupWindow.dismiss();
            }
        });
        editTag = popupView.findViewById(R.id.edit_tag_item);
        editTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TagDialogue(db.getTags(), tagAdapter).show(getSupportFragmentManager(),"Tags");

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

    @Override
    public void onOKPressed(Item item) {
        db.addItem(item);
    }

    @Override
    public void onEditPressed(Item item) {
        itemAdapter.notifyDataSetChanged();
    }
    @Override
    public void onCancelPressed(){

    }

    /**
     * This method initializes the Firestore database and sets up a snapshot listener on the "items" collection.
     * The snapshot listener updates the dataList and notifies the itemAdapter whenever the data in the "items" collection changes.
     * @throws FirebaseFirestoreException if any Firebase Firestore operation fails.
     */
    /*private void handleDatabase() {
        db = FirebaseFirestore.getInstance();
        itemsRef = db.collection("items");

        // Add a snapshot listener to the Firestore reference 'itemsRef'
        itemsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                        dataList.add(new Item(name, date, itemValue, make, model, description, comment, serialNumber));
                    }

                    // refresh ListView and display the new data
                    itemAdapter.notifyDataSetChanged();
                }
            }
        });
    }*/
}

