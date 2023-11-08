package com.example.team29project;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;

/**
 * Display details of a selected inventory item
 * Allow users to view and potentially edit the item's information
 */
public class DisplayActivity extends AppCompatActivity implements InputFragment.OnFragmentsInteractionListener, SelectListener,PickCameraDialog.ImageOrGalleryListener {

    private Button back_button, edit_button;
    private TextView item_name, item_value, item_date, item_make, item_model, item_serialno, item_description, item_comment;
    private Item item;
    ChipGroup tagGroup;
    ArrayList<Uri> uriList = new ArrayList<>();

    RecyclerView imageListView;  //
    MultiImageAdapter adapter;
    Intent cameraIntent , galleryIntent;
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
                            uriList.add(data.getData());
                            adapter.notifyDataSetChanged();
                        } else {
                            for (int i = 0; i < clipData.getItemCount(); i++) {
                                Uri imageUri = clipData.getItemAt(i).getUri();
                                try {
                                    uriList.add(imageUri);

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
       // Intent intent = getIntent();
        back_button = findViewById(R.id.back_button);
        edit_button = findViewById(R.id.edit_button);
        tagGroup = findViewById(R.id.tagGroup);
        item_name = findViewById(R.id.item_name);
        item_value = findViewById(R.id.item_value);
        item_date = findViewById(R.id.item_date);
        item_make = findViewById(R.id.item_make);
        item_model = findViewById(R.id.item_model);
        item_serialno = findViewById(R.id.item_serialno);
        item_description = findViewById(R.id.item_description);
        item_comment = findViewById(R.id.item_comment);
        imageListView= findViewById(R.id.photo_view);
     //   item = intent.getParcelableExtra("item");
       // changeData();
        galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        galleryIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        cameraIntent = new Intent(DisplayActivity.this, CustomCameraActivity.class);
        if(uriList.size()==0){
            int resourceId = R.drawable.plus;
            Resources resources = getResources();
            Uri uris = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                    "://" + resources.getResourcePackageName(resourceId)
                    + '/' + resources.getResourceTypeName(resourceId)
                    + '/' + resources.getResourceEntryName(resourceId));

            uriList.add(uris);
        }
       adapter = new MultiImageAdapter(uriList, getApplicationContext(),this);
       imageListView.setAdapter(adapter);
       imageListView.setLayoutManager(new LinearLayoutManager(DisplayActivity.this, LinearLayoutManager.HORIZONTAL, false));








        // set tags
       /* ArrayList<Tag> tags = item.getTags();
        for (Tag tag: tags) {
            Chip chip = (Chip) LayoutInflater.from(DisplayActivity.this).inflate(R.layout.activity_display, null);
            chip.setText(tag.getName());
            chip.setId(tags.indexOf(tag));
            tagGroup.addView(chip);
        }
        */

        // TODO assign values of photos from item to photos
      //  ArrayList<Bitmap> photos = item.getPhotos();

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /*edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new InputFragment(item).show(getSupportFragmentManager(), "Edit");
            }
        });

         */


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

    private void changeData() {
        item_name.setText(item.getName());
        item_value.setText(item.getValue());
        item_date.setText(item.getDate());
        item_make.setText(item.getMake());
        item_model.setText(item.getModel());
        item_serialno.setText(item.getSerialNumber());
        item_description.setText(item.getDescription());
        item_comment.setText(item.getComment());
        // TODO update photos and tags
    }
    @Override
    public void onItemClick(int position) {
        if(position ==0) {
            new PickCameraDialog().show(getSupportFragmentManager(),"Photo");

        }
        else {
            uriList.remove(position);
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onGalleryPressed() {
        pictureActivityResultLauncher.launch(galleryIntent);

    }

    @Override
    public void onCameraPressed() {
        pictureActivityResultLauncher.launch(cameraIntent);
    }
}