package com.github.eyers.activities;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.eyers.DBOperations;
import com.github.eyers.EyeRS;
import com.github.eyers.ItemLabel;
import com.github.eyers.R;
import com.github.eyers.info.CategoryInfo;
import com.github.eyers.info.ItemInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import static com.github.eyers.EyeRS.REQUEST_READ_EXTERNAL_STORAGE;

public class UpdateItemActivity extends AppCompatActivity implements OnItemSelectedListener, OnClickListener {

    /**
     * A little hacky but  we need to get this to work. Need to fix later.
     */
    public static ItemLabel EDIT_ITEM = null;
    /**
     * Content resolver object
     */
    private ContentResolver eyeRSContentResolver;
    /**
     * Fields & other declarations
     */
    private static String itemName;
    private static String itemDesc;
    private String category;
    private EditText txtTitle;
    private EditText txtDesc;
    private Spinner categorySpinner;
    private String img;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private ImageView ivImage;
    private String userChoosenTask;
    private Bitmap thumbnail;
    public static ArrayAdapter<String> categoriesAdapter;
    String itemToUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.txtTitle = (EditText) findViewById(R.id.edtTxtItemTitleUpdate);
        this.txtDesc = (EditText) findViewById(R.id.edtTxtItemDescUpdate);
        this.categorySpinner = (Spinner) findViewById(R.id.category_spinnerUpdate);
        this.categorySpinner.setOnItemSelectedListener(this); //spinner click listener

        findViewById(R.id.btnUpdateItem).setOnClickListener(this);

        this.ivImage = (ImageView) findViewById(R.id.update_item_image);
        this.ivImage.setOnClickListener(this);

        try {

            if (ContextCompat.checkSelfPermission(UpdateItemActivity.this,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(UpdateItemActivity.this,
                        Manifest.permission.CAMERA)) {
                } else {
                    ActivityCompat.requestPermissions(UpdateItemActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            REQUEST_READ_EXTERNAL_STORAGE);
                }
            }

        } catch (Exception ex) {

            Log.e("CAMERA PERMISSIONS", "Retrieved permission for in-built camera use");
        }

        fillCategories();

        if (EDIT_ITEM != null) {
            txtTitle.setText(EDIT_ITEM.getName());
            ivImage.setImageBitmap(EDIT_ITEM.getImage());
            txtDesc.setText(EDIT_ITEM.getDescription());

            thumbnail = EDIT_ITEM.getImage();
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

            img = Base64.encodeToString(bytes.toByteArray(), Base64.DEFAULT);
        }
    }

    /**
     * Method to populate the spinner.
     */
    private void fillCategories() {

        List<String> categoriesDropDown = retrieveCategories();
        /*
         * Spinner adapter
         */
        categoriesAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, categoriesDropDown);
        this.categorySpinner.setAdapter(categoriesAdapter);
    }

    /**
     * Method returns the categories result set of the SQL query and adds elements into a
     * list structure for the spinner.
     */
    private List<String> retrieveCategories() {

        List<String> categories = new ArrayList<>();

        /*
         * Content resolver object
         */
        eyeRSContentResolver = this.getContentResolver();

        String[] projection = {
                CategoryInfo.CATEGORY_ID,
                CategoryInfo.CATEGORY_NAME
        };

        String whereClause = "";
        String[] whereArgs = {};
        String sortOrder = CategoryInfo.CATEGORY_NAME;
        TreeSet<String> data = new TreeSet<>();

        try {

            /*
             * Content resolver query
             */
            Cursor cursor = eyeRSContentResolver.query(
                    DBOperations.CONTENT_URI_CATEGORIES,
                    projection,
                    whereClause,
                    whereArgs,
                    sortOrder);

            if (!cursor.moveToFirst()) {

                Toast.makeText(this, "Oops something happened there!", Toast.LENGTH_SHORT).show();
                Log.e("NewItemActivity", "Null Cursor object");

            } else if (cursor.moveToFirst()) {

                do {

                    data.add(cursor.getString(cursor.getColumnIndex(CategoryInfo.CATEGORY_NAME)));

                } while (cursor.moveToNext());

                cursor.close();

            } else {

                Log.e("NewItemActivity", "Empty categories list");
            }

        } catch (Exception ex) {

            Log.e("Categories list", ex.getMessage(), ex);
        } finally {

            categories.addAll(data);
        }
        return categories; //return the list of categories
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        //Category selected from Spinner
        category = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View view) {

        try {

            switch (view.getId()) {

                case R.id.btnUpdateItem: { //user clicks add

                    updateItem(itemToUpdate);
                    break;

                }
                case R.id.update_item_image:
                    selectImage();
                    break;
            }

        } catch (Exception ex) {

            Log.e("NewItemActivity", ex.getMessage(), ex);
        }
    }

    /**
     * Same as {@link #startActivity(Intent, Bundle)} with no options
     * specified.
     *
     * @param intent The intent to start.
     * @throws android.content.ActivityNotFoundException
     * @see #startActivity(Intent, Bundle)
     * @see #startActivityForResult
     */
    @Override
    public void startActivity(Intent intent) {

        if (EDIT_ITEM != null) {


            String itemName = EDIT_ITEM.getName();
            String whereClause = "";
            String[] whereArgs = {};
            String sortOrder = ItemInfo.ITEM_NAME;

            /*
             * Content resolver object
             */
            eyeRSContentResolver = this.getContentResolver();

            String[] projection = {
                    ItemInfo.ITEM_ID,
                    ItemInfo.CATEGORY_NAME,
                    ItemInfo.ITEM_NAME,
                    ItemInfo.ITEM_DESC,
                    ItemInfo.ITEM_IMAGE
            };

            try {

                /*
                 * Content resolver query
                 */
                Cursor cursor = eyeRSContentResolver.query(
                        DBOperations.CONTENT_URI_ITEMS,
                        projection,
                        whereClause,
                        whereArgs,
                        sortOrder);

                if (!cursor.moveToFirst()) {

                    Log.e("UpdateItemActivity", "Null Cursor object");

                } else if (cursor.moveToFirst()) {

                    do {

                        if (cursor.getString(cursor.getColumnIndex(ItemInfo.ITEM_NAME))
                                .equals(itemName)) {

                            /*
                             * Retrieves the id of the item to be deleted
                             */
                            itemToUpdate = cursor.getString(cursor.getColumnIndex(ItemInfo.ITEM_ID));
                            updateItem(itemToUpdate);

                            if ((cursor.getString(cursor.getColumnIndex(ItemInfo.CATEGORY_NAME)))
                                    .equals("BOOKS")) {
                                category = "BOOKS";
                            }
                            if ((cursor.getString(cursor.getColumnIndex(ItemInfo.CATEGORY_NAME)))
                                    .equals("CLOTHES")) {
                                category = "CLOTHES";
                            }
                            if ((cursor.getString(cursor.getColumnIndex(ItemInfo.CATEGORY_NAME)))
                                    .equals("ACCESSORIES")) {
                                category = "ACCESSORIES";
                            }
                            if ((cursor.getString(cursor.getColumnIndex(ItemInfo.CATEGORY_NAME)))
                                    .equals("GAMES")) {
                                category = "GAMES";
                            }
                            if ((cursor.getString(cursor.getColumnIndex(ItemInfo.CATEGORY_NAME)))
                                    .equals("OTHER")) {
                                category = "OTHER";
                            }

                        } else {

                            Log.e("ViewItemActivity", "Sorry that item doesn't exist");
                        }

                        cursor.close();

                    } while (cursor.moveToNext());
                }

            } catch (Exception ex) {
                Log.e("ViewItemActivity", "Unable to retrieve item details");
            }

        }
    }

    /**
     * Method to update the item if a matching item exists,
     * otherwise nothing happens
     */
    public void updateItem(String itemToUpdate) {

        try {

            /*
             * To update the item simply specify the item's ID in the where clause
             */
            ContentValues itemValues = new ContentValues();
            String updateWhereClause = ItemInfo.ITEM_ID + " = ?";
            String[] updateWhereArgs = {itemToUpdate};

            /*
             * Content resolver object
             */
            eyeRSContentResolver = this.getContentResolver();

            itemValues.put(ItemInfo.ITEM_NAME, itemName); //item's name
            itemValues.put(ItemInfo.ITEM_DESC, itemDesc); //item's description
            itemValues.put(ItemInfo.ITEM_IMAGE, img); //item's image

            /*
             * Content resolver update operation
             */
            eyeRSContentResolver.update(
                    DBOperations.CONTENT_URI_ITEMS,
                    itemValues,
                    updateWhereClause,
                    updateWhereArgs
            );

            Log.e("DATABASE OPERATIONS", "...Item details updated successfully!");

            /*
             * Then clear the fields after successfully inserting the data
             */
            txtTitle.setText("");
            txtDesc.setText("");
            img = "";
            ivImage.setImageBitmap(null);

        } catch (Exception ex) {

            Log.e("NewItemActivity", ex.getMessage(), ex);

        } finally {

            Toast.makeText(this, "Your item was updated successfully",
                    Toast.LENGTH_SHORT).show();
            MainActivity.STATE = "main";
            super.startActivity(new Intent(this, MainActivity.class));
        }
    }

    @Override
    public void onBackPressed() {
        EDIT_ITEM = null;
        MainActivity.STATE = "main";
        super.startActivity(new Intent(this, MainActivity.class));
        super.finish();
    }

    private void selectImage() {
        final String[] items = {"Take Photo", /*"Choose from Library",*/ "Cancel"};

        try {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add Photo!");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    boolean result = EyeRS.checkPermission(UpdateItemActivity.this);

                    if (items[item].equals("Take Photo")) {
                        userChoosenTask = "Take Photo";
                        if (result) {
                            cameraIntent();
                        }
                    }
//                    else if (items[item].equals("Choose from Library")) {
//                        userChoosenTask = "Choose from Library";
//                        if (result)
//                            galleryIntent();
//
//                    }
                    else if (items[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        } catch (Exception ex) {

            Log.e("selectImage()", ex.getMessage(), ex);
        }
    }


    private void galleryIntent() {
        try {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);//
            startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);

        } catch (Exception ex) {

            Log.e("Gallery intent", ex.getMessage(), ex);
        }
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(intent, REQUEST_CAMERA);
        } catch (Exception ex) {
            Log.e("Camera Permissions", ex.getMessage(), ex);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {

            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == SELECT_FILE) {
                    onSelectFromGalleryResult(data);
                } else if (requestCode == REQUEST_CAMERA) {
                    onCaptureImageResult(data);
                }
            }

        } catch (Exception ex) {

            Log.e("Camera request", ex.getMessage(), ex);
        }
    }

    private void onCaptureImageResult(Intent data) {

        try {

            thumbnail = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

            img = Base64.encodeToString(bytes.toByteArray(), Base64.DEFAULT);

            ivImage.setImageBitmap(thumbnail);

        } catch (Exception ex) {

            Log.e("Image capture", ex.getMessage(), ex);
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        try {

            Bitmap bm = null;
            if (data != null) {
                try {
                    bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            ivImage.setImageBitmap(bm);

        } catch (Exception ex) {

            Log.e("Gallery selection", ex.getMessage(), ex);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        try {
            switch (requestCode) {
                case REQUEST_READ_EXTERNAL_STORAGE:
                    try {
                        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                            if (userChoosenTask.equals("Take Photo"))
                                cameraIntent();
                            else if (userChoosenTask.equals("Choose from Library"))
                                galleryIntent();

                        } else {
                            //code for deny
                        }
                    } catch (Exception ex) {
                        Log.e("CAMERA PERMISSIONS", "Retrieved permission for in-built camera use", ex);
                    }
                    break;
            }
        } catch (Exception ex) {
            Log.e("onReqPermissionResult()", ex.getMessage(), ex);
        }
    }
}

