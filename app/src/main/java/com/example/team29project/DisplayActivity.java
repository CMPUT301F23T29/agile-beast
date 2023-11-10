package com.example.team29project;

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
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;

/**
 * Display details of a selected inventory item
 * Allow users to view and potentially edit the item's information
 */
public class DisplayActivity extends AppCompatActivity implements InputFragment.OnFragmentsInteractionListener, SelectListener,PickCameraDialog.ImageOrGalleryListener {

    private TextView itemName, itemValue, itemDate, itemMake, itemModel, itemSerialno, itemDescription, itemComment;
    private Item item;
    ChipGroup tagGroup;

    RecyclerView imageListView;  //
    MultiImageAdapter adapter;
    Intent cameraIntent , galleryIntent;
    ArrayList<String> photo_string ;

    ArrayList<String> tags;
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
                            photo_string.add(data.getData().toString());
                            adapter.notifyDataSetChanged();
                        } else {
                            for (int i = 0; i < clipData.getItemCount(); i++) {
                                Uri imageUri = clipData.getItemAt(i).getUri();
                                try {
                                    photo_string.add(imageUri.toString());

                                } catch (Exception e) {
                                    Log.e(TAG, "File select error", e);
                                }
                            }
                            adapter.notifyDataSetChanged();

                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        Intent ints = getIntent();
        item = (Item) ints.getSerializableExtra("item");
        tags = ints.getStringArrayListExtra("tags");
        photo_string = item.getPhotos();
        Button backBton = findViewById(R.id.back_button);
        Button editBton = findViewById(R.id.edit_button);
        tagGroup = findViewById(R.id.tagGroup);
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
        cameraIntent = new Intent(DisplayActivity.this, CustomCameraActivity.class);

        if(photo_string.size()==0){
            int resourceId = R.drawable.plus;
            Resources resources = getResources();
            Uri uris = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                    "://" + resources.getResourcePackageName(resourceId)
                    + '/' + resources.getResourceTypeName(resourceId)
                    + '/' + resources.getResourceEntryName(resourceId));

            photo_string.add(uris.toString());
        }
       adapter = new MultiImageAdapter(photo_string, getApplicationContext(),this);
       imageListView.setAdapter(adapter);
       imageListView.setLayoutManager(new LinearLayoutManager(DisplayActivity.this, LinearLayoutManager.HORIZONTAL, false));
       changeData();



        backBton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra( "changed_item",item);
                setResult(Activity.RESULT_OK,resultIntent);
                finish();
            }
        });

        editBton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new InputFragment(item,tags).show(getSupportFragmentManager(), "Edit");
            }
        });

    }

    private void changeData() {
        itemName.setText(item.getName());
        itemValue.setText(item.getValue().toString());
        itemDate.setText(item.getDate());
        itemMake.setText(item.getMake());
        itemModel.setText(item.getModel());
        itemSerialno.setText(item.getSerialNumber());
        itemDescription.setText(item.getDescription());
        itemComment.setText(item.getComment());
        adapter.notifyDataSetChanged();
        // update tags


    }

    @Override
    public void onOKPressed(Item aitem) {
        assert item != null;
        this.item = aitem;
        changeData();
        Intent inte = new Intent(DisplayActivity.this, MainActivity.class);
        inte.putExtra("new_item", item);
        setResult(Activity.RESULT_OK, inte);
    }

    @Override
    public void onEditPressed(Item item) {
        changeData();

    }
    @Override
    public void onCancelPressed(){
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onItemClick(int position) {
        if(position ==0) {
            new PickCameraDialog().show(getSupportFragmentManager(),"Photo");

        }
        else {
            photo_string.remove(position);
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onGalleryPressed() {
        pictureActivityResultLauncher.launch(galleryIntent);
    }

    @Override
    public void onCameraPressed() {pictureActivityResultLauncher.launch(cameraIntent);
    }
}