package com.example.team29project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Item> dataList;
    private ListView itemsList;
    private ArrayAdapter<Item> itemAdapter;
    private FirebaseFirestore db;
    private CollectionReference itemsRef;

    private void handleDatabase() {
        db = FirebaseFirestore.getInstance();
        itemsRef = db.collection("items");

        itemsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firebase", error.toString());
                }

                if (value != null) {
                    dataList.clear();
                    for (QueryDocumentSnapshot doc: value) {
                        String name = doc.getId();
                        String date = doc.getString("date");
                        Number itemValue = doc.getDouble("value");
                        String make = doc.getString("make");
                        String model = doc.getString("model");
                        String serialNumber = doc.getString("serialNumber");
                        String description = doc.getString("description");
                        String comment = doc.getString("comment");

                        dataList.add(new Item(name, date, itemValue, make, model,
                                serialNumber, description, comment));
                    }
                    itemAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init db, load db items into dataList and adapter
        handleDatabase();

        // Init list and
        dataList = new ArrayList<>();

        itemAdapter = new ItemArrayAdapter(this, dataList);
        itemsList = findViewById(R.id.items_list);
        itemsList.setAdapter(itemAdapter);

    }
}