package com.github.eyers.activities;

import android.Manifest;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
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
import android.view.KeyEvent;
import android.view.View;
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
import com.github.eyers.activities.settings.SettingUtilities;
import com.github.eyers.info.CategoryInfo;
import com.github.eyers.info.ItemInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import static com.github.eyers.EyeRS.REQUEST_READ_EXTERNAL_STORAGE;

/**
 * This class enables a user to add a new item and inserts it into the SQLite database.
 */
public class NewItemActivity extends AppCompatActivity implements View.OnClickListener,
        OnItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * A little hacky but  we need to get this to work. Need to fix later.
     */
    public static ItemLabel EDIT_ITEM = null;

    public static ArrayAdapter<String> categoriesAdapter;

    // Fields & other declarations
    private static String itemName;
    private static String itemDesc;
    private String category;
    /**
     * Categories list declaration
     */
    private EditText txtTitle;
    private EditText txtDesc;
    private Spinner categorySpinner;
    // Camera declarations
    private String img;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private ImageView ivImage;
    private String userChoosenTask;
    private Bitmap thumbnail;
    /**
     * Content resolver object
     */
    private ContentResolver eyeRSContentResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SettingUtilities.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_new_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.txtTitle = (EditText) findViewById(R.id.edtTxtItemTitle);
        this.txtDesc = (EditText) findViewById(R.id.edtTxtItemDesc);
        this.categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        this.categorySpinner.setOnItemSelectedListener(this); //spinner click listener

        findViewById(R.id.btnAddItem).setOnClickListener(this);

        this.ivImage = (ImageView) findViewById(R.id.new_item_image);
        this.ivImage.setOnClickListener(this);

        /*
         * Retrieve the saved state values before the activity was destroyed
         */
        if (savedInstanceState != null) {

            categorySpinner.setSelection(savedInstanceState.getInt("category_spinner"));
            txtTitle.setText(savedInstanceState.getString("item_name"));
            txtDesc.setText(savedInstanceState.getString("item_desc"));

        }

        try {

            if (ContextCompat.checkSelfPermission(NewItemActivity.this,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(NewItemActivity.this,
                        Manifest.permission.CAMERA)) {
                } else {
                    ActivityCompat.requestPermissions(NewItemActivity.this,
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
                CategoryInfo.CATEGORY_NAME,
                CategoryInfo.CATEGORY_ICON
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
        }

        categories.addAll(data);
//        for (String str : data) {
//            categories.add(str);
//        }

        return categories; //return the list of categories
    }

    /**
     * Method allows us to save the activity's selections just before the app gets paused.
     */
    public void onPause() {
        super.onPause();

        //Save the spinner's selection
        categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        SharedPreferences category_prefs = getSharedPreferences("category_prefs", Context.MODE_PRIVATE);
        category_prefs.edit().putInt("spinner_indx", categorySpinner.getSelectedItemPosition()).apply();

    }

    /**
     * Method allows us to retrieve previous selection before the activity was paused.
     */
    @Override
    protected void onResume() {

        /*
         * Retrieve the saved spinner selection
         */
        super.onResume();
        categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        SharedPreferences category_prefs = getSharedPreferences("category_prefs", Context.MODE_PRIVATE);
        int spinner_index = category_prefs.getInt("spinner_indx", 0);
        categorySpinner.setSelection(spinner_index);

    }

    /**
     * Called when a view has been clicked.
     *
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {

        try {

            switch (view.getId()) {

                case R.id.btnAddItem: //user clicks add
                    switch (categorySpinner.getSelectedItem().toString().toLowerCase()) {
                        case "books": { // if the user is adding a book item
                            /*
                             * Retrieve user input from fields
                             */
                            itemName = txtTitle.getText().toString();
                            itemDesc = txtDesc.getText().toString();
                                /*
                                 * Empty category
                                */
                            if (category.isEmpty()) {

                                Toast.makeText(this, "Please select a category from the spinner",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            }
                                /*
                                 * Empty item name
                                */
                            else if (itemName.isEmpty()) {

                                Toast.makeText(this, "Please give the item a name",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            }
                                /*
                                * Empty item description
                                */
                            else if (itemDesc.isEmpty()) {

                                Toast.makeText(this, "Please give the item a description",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            }
                                /*
                                * No image added
                                */
                            else if (img.isEmpty()) {

                                Toast.makeText(this, "Please add an image for the item",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            }
                            addBook(); // call the method to add a book item to the db
                        }
                        return;
                        case "clothes": { // if the user is adding a clothing item
                                /*
                                 * Retrieve user input from fields
                                */
                            itemName = txtTitle.getText().toString();
                            itemDesc = txtDesc.getText().toString();
                                /*
                                 * Empty category
                                */
                            if (category.isEmpty()) {

                                Toast.makeText(this, "Please select a category from the spinner",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            }
                                /*
                                 * Empty item name
                                */
                            else if (itemName.isEmpty()) {

                                Toast.makeText(this, "Please give the item a name",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            }
                                /*
                                * Empty item description
                                */
                            else if (itemDesc.isEmpty()) {

                                Toast.makeText(this, "Please give the item a description",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            }
                                /*
                                * No image added
                                */
                            else if (img.isEmpty()) {

                                Toast.makeText(this, "Please add an image for the item",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            }
                            addClothing(); // call the method to add a clothing item to the db
                        }
                        return;
                        case "accessories": { // if the user is adding an accessory item
                                /*
                                 * Retrieve user input from fields
                                */
                            itemName = txtTitle.getText().toString();
                            itemDesc = txtDesc.getText().toString();
                                /*
                                 * Empty category
                                */
                            if (category.isEmpty()) {

                                Toast.makeText(this, "Please select a category from the spinner",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            }
                                /*
                                 * Empty item name
                                */
                            else if (itemName.isEmpty()) {

                                Toast.makeText(this, "Please give the item a name",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            }
                                /*
                                * Empty item description
                                */
                            else if (itemDesc.isEmpty()) {

                                Toast.makeText(this, "Please give the item a description",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            }
                                /*
                                * No image added
                                */
                            else if (img.isEmpty()) {

                                Toast.makeText(this, "Please add an image for the item",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            }
                            addAccessory(); // call the method to add an accessory item to the db
                        }
                        return;
                        case "games": { //if the user is adding a gaming item
                                /*
                                 * Retrieve user input from fields
                                */
                            itemName = txtTitle.getText().toString();
                            itemDesc = txtDesc.getText().toString();
                                /*
                                 * Empty category
                                */
                            if (category.isEmpty()) {

                                Toast.makeText(this, "Please select a category from the spinner",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            }
                            /*
                            * Empty item name
                            */
                            else if (itemName.isEmpty()) {

                                Toast.makeText(this, "Please give the item a name",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            }
                            /*
                             * Empty item description
                            */
                            else if (itemDesc.isEmpty()) {

                                Toast.makeText(this, "Please give the item a description",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            }
                            /*
                             * No image added
                            */
                            else if (img.isEmpty()) {

                                Toast.makeText(this, "Please add an image for the item",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            }
                            addGame();
                        }
                        return;
                        case "other": { // call the method to add another item to the db
                            /*
                             * Retrieve user input from fields
                            */
                            itemName = txtTitle.getText().toString();
                            itemDesc = txtDesc.getText().toString();
                            /*
                             * Empty category
                            */
                            if (category.isEmpty()) {

                                Toast.makeText(this, "Please select a category from the spinner",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            }
                            /*
                             * Empty item name
                            */
                            else if (itemName.isEmpty()) {

                                Toast.makeText(this, "Please give the item a name",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            }
                            /*
                             * Empty item description
                            */
                            else if (itemDesc.isEmpty()) {

                                Toast.makeText(this, "Please give the item a description",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            }
                            /*
                             * No image added
                            */
                            else if (img.isEmpty()) {

                                Toast.makeText(this, "Please add an image for the item",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            }
                            addOther(); // call the method to add a gaming item to the db
                        }
                        return;
                        default: {
                            /*
                         * Retrieve user input from fields
                         */
                            itemName = txtTitle.getText().toString();
                            itemDesc = txtDesc.getText().toString();
                        /*
                        * Empty category
                        */
                            if (category.isEmpty()) {

                                Toast.makeText(this, "Please select a category from the spinner",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            }
                        /*
                         * Empty item name
                         */
                            else if (itemName.isEmpty()) {

                                Toast.makeText(this, "Please give the item a name",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            }
                        /*
                         * Empty item description
                         */
                            else if (itemDesc.isEmpty()) {

                                Toast.makeText(this, "Please give the item a description",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            }
                        /*
                         * No image added
                         */
                            else if (img.isEmpty()) {

                                Toast.makeText(this, "Please add an image for the item",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            }

                            addItemInfo(); // call the method to add a user specified-item to the db
                        }
                        return;
                    }
                case R.id.new_item_image:
                    selectImage();
                    break;
            }

        } catch (Exception ex) {

            Log.e("NewItemActivity", ex.getMessage(), ex);
        }

    }

    /**
     * Adding a book item.
     */
    private void addBook() {

        /*
         * Define an object to contain the new values to insert
         */
        ContentValues bookValues = new ContentValues();

        bookValues.put(ItemInfo.CATEGORY_NAME, category); //Book category
        bookValues.put(ItemInfo.ITEM_NAME, itemName); //Book name
        bookValues.put(ItemInfo.ITEM_DESC, itemDesc); //Book description
        bookValues.put(ItemInfo.ITEM_IMAGE, img); //Book image

        /*
         * Content resolver object
         */
        eyeRSContentResolver = this.getContentResolver();

        try {

            /*
             * Content resolver book insert
             */
            eyeRSContentResolver.insert(
                    DBOperations.CONTENT_URI_ITEMS,
                    bookValues);

            Toast.makeText(this, "Your book item has been added successfully ", Toast.LENGTH_SHORT).show();
            //Display message in the logcat window after successful operation execution
            Log.e("DATABASE OPERATIONS", "...Book item added to DB!");

            /*
             * Then clear the fields after successfully inserting the data
             */
            txtTitle.setText("");
            txtDesc.setText("");
            img = "";
            ivImage.setImageBitmap(null);
            startActivity(new Intent(this, MainActivity.class));

        } catch (Exception ex) {

            Toast.makeText(this, "Unable to add item the book", Toast.LENGTH_SHORT).show();
            Log.e(getClass().getSimpleName(), "Book item not added.", ex);
            txtTitle.setText("");
            txtDesc.setText("");
            img = "";
            ivImage.setImageBitmap(null);

        }

    } //end void addBook()

    /**
     * Adding a clothing item.
     */
    private void addClothing() {
        /*
         * Define an object to contain the new values to insert.
         */
        ContentValues clothesValues = new ContentValues();

        clothesValues.put(ItemInfo.CATEGORY_NAME, category); //Clothing category
        clothesValues.put(ItemInfo.ITEM_NAME, itemName); //Clothing name
        clothesValues.put(ItemInfo.ITEM_DESC, itemDesc); //Clothing description
        clothesValues.put(ItemInfo.ITEM_IMAGE, img); //Clothing image

        /*
         * Content resolver object
         */
        eyeRSContentResolver = this.getContentResolver();

        try {

            /*
             * Content resolver clothes insert
             */
            eyeRSContentResolver.insert(
                    DBOperations.CONTENT_URI_ITEMS,
                    clothesValues);

            Toast.makeText(this, "Your clothing item has been added successfully ", Toast.LENGTH_SHORT).show();
            //Display message in the logcat window after successful operation execution
            Log.e("DATABASE OPERATIONS", "...Clothing item added to DB!");

            /*
             * Then clear the fields after successfully inserting the data
             */
            txtTitle.setText("");
            txtDesc.setText("");
            img = "";
            ivImage.setImageBitmap(null);
            startActivity(new Intent(this, MainActivity.class));

        } catch (Exception ex) {

            Toast.makeText(this, "Unable to add item", Toast.LENGTH_SHORT).show();
            Log.e(getClass().getSimpleName(), "Clothing item not added.", ex);
            txtTitle.setText("");
            txtDesc.setText("");
            img = "";
            ivImage.setImageBitmap(null);

        }

    } //end void addClothing()

    /**
     * Adding an accessory item.
     */
    private void addAccessory() {

        /**
         * Define an object to contain the new values to insert
         */
        ContentValues accessoriesValues = new ContentValues();

        accessoriesValues.put(ItemInfo.CATEGORY_NAME, category); // Accessory category
        accessoriesValues.put(ItemInfo.ITEM_NAME, itemName);     // Accessory name
        accessoriesValues.put(ItemInfo.ITEM_DESC, itemDesc);     // Accessory description
        accessoriesValues.put(ItemInfo.ITEM_IMAGE, img);         // Accessory image

        /*
         * Content resolver object
         */
        eyeRSContentResolver = this.getContentResolver();

        try {

            /*
             * Content resolver accessories insert
             */
            eyeRSContentResolver.insert(
                    DBOperations.CONTENT_URI_ITEMS,
                    accessoriesValues);

            Toast.makeText(this, "Your accessory item has been added successfully ", Toast.LENGTH_SHORT).show();
            //Display message in the logcat window after successful operation execution
            Log.e("DATABASE OPERATIONS", "...Accessory item added to DB!");

            /*
             * Then clear the fields after successfully inserting the data
             */
            txtTitle.setText("");
            txtDesc.setText("");
            img = "";
            ivImage.setImageBitmap(null);
            startActivity(new Intent(this, MainActivity.class));
        } catch (Exception ex) {
            Toast.makeText(this, "Unable to add item", Toast.LENGTH_SHORT).show();
            Log.e(getClass().getSimpleName(), "Accessory item not added.", ex);
            txtTitle.setText("");
            txtDesc.setText("");
            img = "";
            ivImage.setImageBitmap(null);
        }
    } //end void addAccessory()

    /**
     * Adding a gaming item.
     */
    private void addGame() {

        /*
         * Define an object to contain the new values to insert
         */
        ContentValues gamesValues = new ContentValues();

        gamesValues.put(ItemInfo.CATEGORY_NAME, category); //Game category
        gamesValues.put(ItemInfo.ITEM_NAME, itemName); //Game name
        gamesValues.put(ItemInfo.ITEM_DESC, itemDesc); //Game description
        gamesValues.put(ItemInfo.ITEM_IMAGE, img); //Game image

        /*
         * Content resolver object
         */
        eyeRSContentResolver = this.getContentResolver();

        try {

            /*
             * Content resolver games insert
             */
            eyeRSContentResolver.insert(
                    DBOperations.CONTENT_URI_ITEMS,
                    gamesValues);

            Toast.makeText(this, "Your gaming item has been added successfully ", Toast.LENGTH_SHORT).show();
            //Display message in the logcat window after successful operation execution
            Log.e("DATABASE OPERATIONS", "...Game item added to DB!");

            /*
             * Then clear the fields after successfully inserting the data
             */
            txtTitle.setText("");
            txtDesc.setText("");
            img = "";
            ivImage.setImageBitmap(null);
            startActivity(new Intent(this, MainActivity.class));

        } catch (Exception ex) {
            Toast.makeText(this, "Unable to add item", Toast.LENGTH_SHORT).show();
            Log.e(getClass().getSimpleName(), "Gaming item not added.", ex);
            txtTitle.setText("");
            txtDesc.setText("");
            img = "";
            ivImage.setImageBitmap(null);
        }
    }
    //end void addGame()

    /**
     * Adding a random item.
     */
    private void addOther() {

        /*
         * Define an object to contain the new values to insert
         */
        ContentValues otherValues = new ContentValues();

        otherValues.put(ItemInfo.CATEGORY_NAME, category); //Other category
        otherValues.put(ItemInfo.ITEM_NAME, itemName); //Other name
        otherValues.put(ItemInfo.ITEM_DESC, itemDesc); //Other description
        otherValues.put(ItemInfo.ITEM_IMAGE, img); //Other image

        /*
         * Content resolver object
         */
        eyeRSContentResolver = this.getContentResolver();

        try {

            /*
             * Content resolver other insert
             */
            eyeRSContentResolver.insert(
                    DBOperations.CONTENT_URI_ITEMS,
                    otherValues);

            Toast.makeText(this, "Your other item has been added successfully ", Toast.LENGTH_SHORT).show();
            //Display message in the logcat window after successful operation execution
            Log.e("DATABASE OPERATIONS", "...Other item added to DB!");

            /*
             * Then clear the fields after successfully inserting the data
             */
            txtTitle.setText("");
            txtDesc.setText("");
            img = "";
            ivImage.setImageBitmap(null);
            startActivity(new Intent(this, MainActivity.class));

        } catch (Exception ex) {

            Toast.makeText(this, "Unable to add item", Toast.LENGTH_SHORT).show();
            Log.e(getClass().getSimpleName(), "Other item not added.", ex);
            txtTitle.setText("");
            txtDesc.setText("");
            img = "";
            ivImage.setImageBitmap(null);
        }
    }

    //end void addOther()

    /**
     * Method to add a new Item.
     */
    public void addItemInfo() {

        /**
         * Define an object to contain the new values to insert.
         */
        ContentValues itemsValues = new ContentValues();

        itemsValues.put(ItemInfo.CATEGORY_NAME, category); //user specified category
        itemsValues.put(ItemInfo.ITEM_NAME, itemName); //item's name
        itemsValues.put(ItemInfo.ITEM_DESC, itemDesc); //item's description
        itemsValues.put(ItemInfo.ITEM_IMAGE, img); //item's image

        /**
         * Content resolver object
         */
        eyeRSContentResolver = this.getContentResolver();

        try {

            /**
             * Content resolver items insert
             */
            eyeRSContentResolver.insert(
                    DBOperations.CONTENT_URI_ITEMS,
                    itemsValues);

            Toast.makeText(this, "Your item has been added successfully", Toast.LENGTH_SHORT).show();
            //Display message in the logcat window after successful operation execution
            Log.e("DATABASE OPERATIONS", "...New item added to DB!");

            /**
             * Then clear the fields after successfully inserting the data
             */
            txtTitle.setText("");
            txtDesc.setText("");
            img = "";
            ivImage.setImageBitmap(null);
            startActivity(new Intent(this, MainActivity.class));

        } catch (Exception ex) {
            Toast.makeText(this, "Unable to add item", Toast.LENGTH_SHORT).show();
            Log.e(getClass().getSimpleName(), "User specified item not added.", ex);
            txtTitle.setText("");
            txtDesc.setText("");
            img = "";
            ivImage.setImageBitmap(null);
        }
    }
    //end void addItemInfo()

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        Toast.makeText(this, requestCode + "", Toast.LENGTH_LONG).show();

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

    private void selectImage() {
        final String[] items = {"Take Photo", /*"Choose from Library",*/ "Cancel"};

        try {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add Photo!");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    boolean result = EyeRS.checkPermission(NewItemActivity.this);

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

    /**
     * Method handles what happens when an item is selected from the spinner.
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        //Category selected from Spinner
        category = parent.getItemAtPosition(position).toString();

    }

    /**
     * @param parent Method handles what happens when nothing is selected from the spinner
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * @param savedInstanceState Save the state of the activity if it's about to be destroyed
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putInt("category_spinner", categorySpinner.getSelectedItemPosition());
        savedInstanceState.putString("item_name", txtTitle.getText().toString());
        savedInstanceState.putString("item_desc", txtDesc.getText().toString());
    }

    /**
     * This is called when a new Loader needs to be created.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    /**
     * A callback method, invoked after the requested content provider returns all the data.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    }

    /**
     * This is called when the last Cursor provided to onLoadFinished() above is about to be closed.
     * We need to make sure we are no longer using it.
     *
     * @param loader
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ECLAIR
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        EDIT_ITEM = null;
        MainActivity.STATE = "main";
        super.startActivity(new Intent(this, MainActivity.class));
        super.finish();
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
            eyeRSContentResolver = this.getContentResolver();

            String[] projection = {
                    ItemInfo.ITEM_ID,
                    ItemInfo.CATEGORY_NAME,
                    ItemInfo.ITEM_NAME,
                    ItemInfo.ITEM_DESC,
                    ItemInfo.ITEM_IMAGE
            };

            String whereClause = "";
            String[] whereArgs = {};
            String sortOrder = ItemInfo.ITEM_NAME;

            String idToDelete = "";

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

                if (cursor.moveToFirst()) {

                    if (cursor.getString(cursor.getColumnIndex(ItemInfo.ITEM_NAME))
                            .equals(EDIT_ITEM.getName().toString())) {

                        /**
                         * Retrieves the id of the item to be deleted
                         */
                        idToDelete = cursor.getString(cursor.getColumnIndex(ItemInfo.ITEM_ID));

                    } else {

                        Log.e("ViewItemActivity", "Sorry that item doesn't exist");
                    }

                    cursor.close();
                }
            } catch (Exception ex) {
                Log.e("ViewItemActivity", "Unable to retrieve item details");
            }

        /*
         * To delete the item simply specify the item's ID in the where clause
         */
            String deleteWhereClause = ItemInfo.ITEM_ID + " = ?";
            String[] deleteWhereArgs = {idToDelete};

        /*
         * Content Resolver delete operation
         */
            eyeRSContentResolver.delete(
                    DBOperations.CONTENT_URI_ITEMS,
                    deleteWhereClause,
                    deleteWhereArgs);
        }
        EDIT_ITEM = null;
        super.startActivity(intent);
    }

} //end class NewItemActivity
