package com.example.team29project.View;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.team29project.Controller.DatabaseController;
import com.example.team29project.Controller.ItemCallback;
import com.example.team29project.Controller.OnPhotoUploadCompleteListener;
import com.example.team29project.Controller.SelectListener;
import com.example.team29project.Model.Item;
import com.example.team29project.Controller.MultiImageAdapter;
import com.example.team29project.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Display details of a selected inventory item
 * Allow users to view and potentially edit the item's information
 */
public class ItemViewActivity extends AppCompatActivity implements
        InputFragment.OnFragmentsInteractionListener,
        SelectListener, PickCameraDialog.ImageOrGalleryListener,
        ItemCallback, OnPhotoUploadCompleteListener

{

    private TextView itemName, itemValue, itemDate, itemMake, itemModel, itemSerialno, itemDescription, itemComment;
    private Item item;
    ChipGroup tagGroup;

    RecyclerView imageListView;
    MultiImageAdapter adapter;
    Intent cameraIntent , galleryIntent;
    ArrayList<String> photo_string ;

    ArrayList<String> tags;
    DatabaseController db;

    // When it gets images from camera or gallery,
    ActivityResultLauncher<Intent> pictureActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        ClipData clipData = data.getClipData();
                        if (clipData == null) {
                            String uniqueId = UUID.randomUUID().toString();
                            photo_string.add(uniqueId);
                            db.uploadPhoto(data.getData(),ItemViewActivity.this,uniqueId,1);
                            // adapter.notifyDataSetChanged();
                        } else {
                            for (int i = 0; i < clipData.getItemCount(); i++) {
                                Uri imageUri = clipData.getItemAt(i).getUri();
                                try {
                                    String uniqueId = UUID.randomUUID().toString();
                                    photo_string.add(uniqueId);
                                    // photo_string.add(imageUri.toString());
                                    db.uploadPhoto(imageUri,ItemViewActivity.this,uniqueId,i+1);
                                } catch (Exception e) {
                                    Log.e(TAG, "File select error", e);
                                }
                            }

                        }
                    }
                    db.updatePhoto(item, photo_string);
                }
            });

    /**
     * Initializes the item detail activity. If the activity is being re-initialized after previously being shut down,
     * this contains the data it most recently supplied in onSaveInstanceState. Otherwise, it is null.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     * this contains the data it most recently supplied in onSaveInstanceState. Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        Intent ints = getIntent();
        String documentId = ints.getStringExtra("documentId");
        String userId = ints.getStringExtra("userId");
        db = new DatabaseController(userId);
        db.getItem(documentId, this);
        Button backBton = findViewById(R.id.back_button);
        Button editBton = findViewById(R.id.edit_button);
        tagGroup = findViewById(R.id.tagGroup);
        tagGroup.setSelectionRequired(false);
        itemName = findViewById(R.id.item_name);
        itemValue = findViewById(R.id.item_value);
        itemDate = findViewById(R.id.item_date);
        itemMake = findViewById(R.id.item_make);
        itemModel = findViewById(R.id.item_model);
        itemSerialno = findViewById(R.id.item_serialno);
        itemDescription = findViewById(R.id.item_description);
        itemComment = findViewById(R.id.item_comment);
        imageListView= findViewById(R.id.photo_view);
        galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        galleryIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        cameraIntent = new Intent(ItemViewActivity.this, CustomCameraActivity.class);


        backBton.setOnClickListener(view -> {
            finish();
        });
        editBton.setOnClickListener(view -> new InputFragment(db,item).show(getSupportFragmentManager(), "Edit"));

    }

    /**
     * This is called when it runs through load initialize items
     * @param newItem Item that is loaded from db
     */
    @Override
    public void onItemLoaded(Item newItem) {
        item = newItem;
        photo_string = item.getPhotos();
        // Default image of adding photos
        if(photo_string.size()==0){
            photo_string.add("https://static.vecteezy.com/system/resources/previews/026/306/461/original/cross-sign-plus-add-addition-math-mathematics-additional-black-and-white-line-icon-symbol-artwork-clipart-illustration-vector.jpg");
        }
        adapter = new MultiImageAdapter(photo_string, getApplicationContext(),this,db);
        imageListView.setAdapter(adapter);
        imageListView.setLayoutManager(new LinearLayoutManager(ItemViewActivity.this, LinearLayoutManager.HORIZONTAL, false));
        changeData();
    }


    @Override
    public void onFailure(Exception e) {
        Toast.makeText(ItemViewActivity.this, "Failed", Toast.LENGTH_SHORT).show();
    }
    /**
     * Change Item's detail
     */
    private void changeData() {
        itemName.setText(item.getName());
        itemValue.setText(String.format("$ %.2f",item.getValue()));
        itemDate.setText(item.getDate());
        itemMake.setText(item.getMake());
        itemModel.setText(item.getModel());
        itemSerialno.setText(item.getSerialNumber());
        itemDescription.setText(item.getDescription());
        itemComment.setText(item.getComment());
        adapter.notifyDataSetChanged();
        tags = item.getTags();
        tagGroup.removeAllViews();
        for(String tag: tags) {
            Chip chip = new Chip(this);
            chip.setText(tag);
            chip.setId(tags.indexOf(tag));
            chip.setCheckable(false);
            chip.setClickable(false);
            tagGroup.addView(chip);
        }
        // update tags


    }

    /**
     * Handles if ok was pressed and changes the data
     *
     */
    @Override
    public void onOKPressed() {
        changeData();
    }

    /**
     * Handles if edit was pressed and changes the data
     */

    /**
     * Handles if cancel was pressed
     */
    @Override
    public void onCancelPressed(){
        adapter.notifyDataSetChanged();
    }


    /**
     * Handles if an item was pressed
     * @param position the position to be used
     */
    @Override
    public void onItemClick(int position) {
        if(position ==0) {
            new PickCameraDialog().show(getSupportFragmentManager(), "Photo");
        }
        else {
            db.deletePhoto(photo_string.get(position));
            photo_string.remove(position);
            db.updatePhoto(item,photo_string);
            adapter.notifyDataSetChanged();
        }
    }
    /**
     * Handles if gallery photo was pressed
     */
    @Override
    public void onGalleryPressed() {
        pictureActivityResultLauncher.launch(galleryIntent);
    }

    /**
     * Handles if the camera was pressed
     */
    @Override
    public void onCameraPressed() {
        pictureActivityResultLauncher.launch(cameraIntent);
    }

    /**
     * Handles when photo is successfully uploaded
     */
    @Override
    public void onPhotoUploadComplete(int position) {
        adapter.notifyItemChanged(position);

    }

    /**
     *  handle when photo upload is failed
     * @param e error  message
     */

    @Override
    public void onPhotoUploadFailure(Exception e) {
        Toast.makeText(this, "Failed to upload Photos", Toast.LENGTH_SHORT).show();

    }


}