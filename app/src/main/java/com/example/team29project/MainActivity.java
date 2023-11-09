package com.example.team29project;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;


import com.google.android.material.navigation.NavigationView;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

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
    private ArrayList<Item> dataList;
    private ListView itemsList;
    private int itemPosition ;
    private boolean isDelete;

    private ArrayList<String> tags;
    private TagAdapter tagAdapter;
    private boolean isSelect;

    private ArrayList<Integer> selectedItems;


   // private FirebaseFirestore db;
    //private CollectionReference itemsRef;
   ActivityResultLauncher<Intent> itemActivityResultLauncher = registerForActivityResult(
           new ActivityResultContracts.StartActivityForResult(),
           new ActivityResultCallback<ActivityResult>() {
               @Override
               public void onActivityResult(ActivityResult result) {
                   if (result.getData() !=null) {
                       Item newItem = (Item) result.getData().getExtras().getSerializable("changed_item");
                       Item temp = dataList.get(itemPosition);
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
        selectedItems = new ArrayList<Integer>();
        isDelete= false;
        isSelect= false;
        dataList = new ArrayList<>();
        tags = new ArrayList<>();
        itemAdapter = new ItemArrayAdapter(this, dataList);
        tagAdapter = new TagAdapter(this,tags);
        itemsList = findViewById(R.id.items);
        itemsList.setAdapter(itemAdapter);
        double a = 11.25;
        dataList.add(new Item("Name","2023-11",11.0,"Apple","Iphone","model5","nice phone","0000000000"));
        itemAdapter.notifyDataSetChanged();
        itemsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position >=0) {
                    if(isSelect){
                        selectedItems.add(position);
                    }

                    else if(isDelete){
                        dataList.remove(position);
                        itemAdapter.notifyDataSetChanged();
                        isDelete= false;
                    }
                    else {
                        itemPosition = position;
                        Item temp = dataList.get(position);
                        Intent display = new Intent(MainActivity.this, DisplayActivity.class);
                        display.putExtra("item", temp);
                        itemActivityResultLauncher.launch(display);
                    }

                }

            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSelect){
                    for(int i = 0; i<selectedItems.size(); i++){
                        dataList.get(selectedItems.get(i)).switchSign();
                    }
                    for(int i = 0; i<dataList.size(); i++){
                        if(dataList.get(i).getDelete()){
                            dataList.remove(i);
                        }
                    }
                    isSelect= false;
                    selectedItems = new ArrayList<Integer>();
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
                for(int i = 0; i<selectedItems.size(); i++){
                    if(dataList.get(selectedItems.get(i)).getDelete()){
                        dataList.get(selectedItems.get(i)).switchSign();
                    }
                }
                isSelect = true;
                isDelete = false;
                popupWindow.dismiss();
                selectedItems = new ArrayList<Integer>();
            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelect =false;
                isDelete =false;
                new InputFragment().show(getSupportFragmentManager(), "addItems");
                popupWindow.dismiss();
            }
        });
        editTag = popupView.findViewById(R.id.edit_tag_item);
        editTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TagDialogue(tags, tagAdapter).show(getSupportFragmentManager(),"Tags");

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
        dataList.add(item);
        itemAdapter.notifyDataSetChanged();

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

