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
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
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
import com.github.eyers.R;
import com.github.eyers.info.NewCategoryInfo;
import com.github.eyers.info.NewItemInfo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
     * Fields & other declarations
     */
    private static String itemName;
    private static String itemDesc;
    private static String category;
    private EditText txtTitle;
    private EditText txtDesc;
    private Spinner categorySpinner;
    /**
     * Categories list declaration
     */
    public List<String> popCategories;
    public static ArrayAdapter<String> categoriesAdapter;
    /**
     * Camera declarations
     */
    String img;
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
        setContentView(R.layout.activity_new_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.txtTitle = (EditText) findViewById(R.id.edtTxtItemTitle);
        this.txtDesc = (EditText) findViewById(R.id.edtTxtItemDesc);
        this.categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        this.categorySpinner.setOnItemSelectedListener(this); //spinner click listener

        populateSpinner();

        findViewById(R.id.btnAddItem).setOnClickListener(this);

        this.ivImage = (ImageView) findViewById(R.id.new_item_image);
        this.ivImage.setOnClickListener(this);

        /**
         * Retrieve the saved state values before the activity was destroyed
         */
        if (savedInstanceState != null) {

            categorySpinner.setSelection(savedInstanceState.getInt("category_spinner"));
            txtTitle.setText(savedInstanceState.getString("item_name"));
            txtDesc.setText(savedInstanceState.getString("item_desc"));

        }

        if (ContextCompat.checkSelfPermission(NewItemActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(NewItemActivity.this,
                    Manifest.permission.CAMERA)) {
            } else {
                ActivityCompat.requestPermissions(NewItemActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        REQUEST_READ_EXTERNAL_STORAGE);
            }
        }
    }

    /**
     * Method to populate the spinner.
     */
    public void populateSpinner() {

        //Spinner categories
        popCategories = getCategoriesList();

        //Create an adapter for the spinner
        categoriesAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, popCategories);

        //Set the adapter to the spinner
        this.categorySpinner.setAdapter(categoriesAdapter);

    }

    /**
     * @return Method returns the categories result set of the SQL query and adds elements into a
     * list structure for the spinner
     */
    public List<String> getCategoriesList() {

        List<String> addCategories = new ArrayList<String>();

        eyeRSContentResolver = this.getContentResolver(); //Content resolver object

        String[] projection = {
                NewCategoryInfo.CategoryInfo.CATEGORY_ID,
                NewCategoryInfo.CategoryInfo.CATEGORY_NAME,
                NewCategoryInfo.CategoryInfo.CATEGORY_DESC,
                NewCategoryInfo.CategoryInfo.CATEGORY_ICON};

        String selection = "category_name = \"" + NewCategoryInfo.CategoryInfo.CATEGORY_NAME
                + "\"";

        Cursor cursor = eyeRSContentResolver.query(DBOperations.CONTENT_URI_CATEGORIES,
                projection, null, null,
                null);

        TreeSet<String> data = new TreeSet<>();

        if (cursor.moveToFirst()) {

            do {

                data.add(cursor.getString(1));

            } while (cursor.moveToNext());

            cursor.close();

        } else {

            addCategories = null; //empty categories list
        }

        for (String str : data) {

            addCategories.add(str);

        }

        return addCategories; //return the list of categories
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
        super.onResume();

        /**
         * Retrieve the saved spinner selection
         */
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

        itemName = txtTitle.getText().toString();
        itemDesc = txtDesc.getText().toString();

        switch (view.getId()) {

            case R.id.btnAddItem: //user clicks add
                if (txtTitle != null) {
                    switch (categorySpinner.getSelectedItem().toString().toLowerCase()) {
                        case "books": { // if the user is adding a book item
                            addBook(); // call the method to add a book item to the db
                        }
                        return;
                        case "clothes": { // if the user is adding a clothing item
                            addClothing(); // call the method to add a clothing item to the db
                        }
                        return;
                        case "accessories": { // if the user is adding an accessory item
                            addAccessory(); // call the method to add an accessory item to the db
                        }
                        return;
                        case "games": { //if the user is adding a gaming item
                            addGame();
                        }
                        return;
                        case "other": { // call the method to add another item to the db
                            addOther(); // call the method to add a gaming item to the db
                        }
                        return;
                        case NewCategoryInfo.CategoryInfo.CATEGORY_NAME: {  // if the user is adding any item that doesn't correspond to the default categories
                            addItemInfo(); // call the method to add a user specified-item to the db
                        }

                    }
                }
                else{
                    Toast.makeText(this, "Sorry but your item was not added successfully",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.new_item_image:
                selectImage();
                break;
        }

    }

    /**
     * Adding a book item.
     */
    private void addBook() {

        /**
         * Define an object to contain the new values to insert
         */
        ContentValues bookValues = new ContentValues();

        bookValues.put(NewItemInfo.ItemInfo.CATEGORY_NAME, category); //Book category
        bookValues.put(NewItemInfo.ItemInfo.ITEM_NAME, itemName); //Book name
        bookValues.put(NewItemInfo.ItemInfo.ITEM_DESC, itemDesc); //Book description
        bookValues.put(NewItemInfo.ItemInfo.ITEM_IMAGE, img); //Book image

        try {

            //Insert the Book item
            eyeRSContentResolver.insert(DBOperations.CONTENT_URI_ITEMS, bookValues);

            Toast.makeText(this, "Your book item has been added successfully ", Toast.LENGTH_SHORT).show();
            //Display message in the logcat window after successful operation execution
            Log.e("DATABASE OPERATIONS", "...Book item added to DB!");

            /**
             * Then clear the fields after successfully inserting the data
             */
            txtTitle.setText("");
            txtDesc.setText("");
            img = "";
            ivImage.setImageBitmap(null);

        } catch (SQLiteException ex) {
            Toast.makeText(this, "Unable to add item", Toast.LENGTH_SHORT).show();
        }

    } //end void addBook()

    /**
     * Adding a clothing item.
     */
    private void addClothing() {

        /**
         * Define an object to contain the new values to insert.
         */
        ContentValues clothesValues = new ContentValues();

        clothesValues.put(NewItemInfo.ItemInfo.CATEGORY_NAME, category); //Clothing category
        clothesValues.put(NewItemInfo.ItemInfo.ITEM_NAME, itemName); //Clothing name
        clothesValues.put(NewItemInfo.ItemInfo.ITEM_DESC, itemDesc); //Clothing description
        clothesValues.put(NewItemInfo.ItemInfo.ITEM_IMAGE, img); //Clothing image

        try {

            //Insert the Clothing item
            eyeRSContentResolver.insert(DBOperations.CONTENT_URI_ITEMS, clothesValues);

            Toast.makeText(this, "Your clothing item has been added successfully ", Toast.LENGTH_SHORT).show();
            //Display message in the logcat window after successful operation execution
            Log.e("DATABASE OPERATIONS", "...Clothing item added to DB!");

            /**
             * Then clear the fields after successfully inserting the data
             */
            txtTitle.setText("");
            txtDesc.setText("");
            img = "";
            ivImage.setImageBitmap(null);

        } catch (SQLiteException ex) {
            Toast.makeText(this, "Unable to add item", Toast.LENGTH_SHORT).show();
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

        accessoriesValues.put(NewItemInfo.ItemInfo.CATEGORY_NAME, category); //Accessory category
        accessoriesValues.put(NewItemInfo.ItemInfo.ITEM_NAME, itemName); //Accessory name
        accessoriesValues.put(NewItemInfo.ItemInfo.ITEM_DESC, itemDesc); //Accessory description
        accessoriesValues.put(NewItemInfo.ItemInfo.ITEM_IMAGE, img); //Accessory image

        try {

            //Insert the Accessory item
            eyeRSContentResolver.insert(DBOperations.CONTENT_URI_ITEMS, accessoriesValues);

            Toast.makeText(this, "Your accessory item has been added successfully ", Toast.LENGTH_SHORT).show();
            //Display message in the logcat window after successful operation execution
            Log.e("DATABASE OPERATIONS", "...Accessory item added to DB!");

            /**
             * Then clear the fields after successfully inserting the data
             */
            txtTitle.setText("");
            txtDesc.setText("");
            img = "";
            ivImage.setImageBitmap(null);

        } catch (SQLiteException ex) {
            Toast.makeText(this, "Unable to add item", Toast.LENGTH_SHORT).show();
        }

    } //end void addAccessory()

    /**
     * Adding a gaming item.
     */
    private void addGame() {

        /**
         * Define an object to contain the new values to insert
         */
        ContentValues gamesValues = new ContentValues();

        gamesValues.put(NewItemInfo.ItemInfo.CATEGORY_NAME, category); //Game category
        gamesValues.put(NewItemInfo.ItemInfo.ITEM_NAME, itemName); //Game name
        gamesValues.put(NewItemInfo.ItemInfo.ITEM_DESC, itemDesc); //Game description
        gamesValues.put(NewItemInfo.ItemInfo.ITEM_IMAGE, img); //Game image

        try {

            //Insert the Game item
            eyeRSContentResolver.insert(DBOperations.CONTENT_URI_ITEMS, gamesValues);

            Toast.makeText(this, "Your gaming item has been added successfully ", Toast.LENGTH_SHORT).show();
            //Display message in the logcat window after successful operation execution
            Log.e("DATABASE OPERATIONS", "...Game item added to DB!");

            /**
             * Then clear the fields after successfully inserting the data
             */
            txtTitle.setText("");
            txtDesc.setText("");
            img = "";
            ivImage.setImageBitmap(null);

        } catch (SQLiteException ex) {
            Toast.makeText(this, "Unable to add item", Toast.LENGTH_SHORT).show();
            Log.e(getClass().getSimpleName(), "Unable to add item", ex);
        }

    } //end void addGame()

    /**
     * Adding a random item.
     */
    private void addOther() {

        /**
         * Define an object to contain the new values to insert
         */
        ContentValues otherValues = new ContentValues();

        otherValues.put(NewItemInfo.ItemInfo.CATEGORY_NAME, category); //Other category
        otherValues.put(NewItemInfo.ItemInfo.ITEM_NAME, itemName); //Other name
        otherValues.put(NewItemInfo.ItemInfo.ITEM_DESC, itemDesc); //Other description
        otherValues.put(NewItemInfo.ItemInfo.ITEM_IMAGE, img); //Other image

        try {

            //Insert the Other item
            eyeRSContentResolver.insert(DBOperations.CONTENT_URI_ITEMS, otherValues);

            Toast.makeText(this, "Your other item has been added successfully ", Toast.LENGTH_SHORT).show();
            //Display message in the logcat window after successful operation execution
            Log.e("DATABASE OPERATIONS", "...Other item added to DB!");

            /**
             * Then clear the fields after successfully inserting the data
             */
            txtTitle.setText("");
            txtDesc.setText("");
            img = "";
            ivImage.setImageBitmap(null);

        } catch (SQLiteException ex) {
            Toast.makeText(this, "Unable to add item", Toast.LENGTH_SHORT).show();
        }

    } //end void addOther()

    /**
     * Method to add a new Item.
     */
    public void addItemInfo() {

        /**
         * Define an object to contain the new values to insert.
         */
        ContentValues itemsValues = new ContentValues();

        itemsValues.put(NewItemInfo.ItemInfo.CATEGORY_NAME, category); //user specified category
        itemsValues.put(NewItemInfo.ItemInfo.ITEM_NAME, itemName); //item's name
        itemsValues.put(NewItemInfo.ItemInfo.ITEM_DESC, itemDesc); //item's description
        itemsValues.put(NewItemInfo.ItemInfo.ITEM_IMAGE, img); //item's image

        try {

            eyeRSContentResolver.insert(DBOperations.CONTENT_URI_ITEMS, itemsValues);

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


        } catch (SQLiteException ex) {
            Toast.makeText(this, "Unable to add item", Toast.LENGTH_SHORT).show();
        }

    } //end void addItemInfo()

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Toast.makeText(this, requestCode + "", Toast.LENGTH_LONG).show();
        switch (requestCode) {
            case REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
        final String[] items = {"Take Photo", "Choose from Library", "Cancel"};

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
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        img = "data:image/jpg;base64," + Base64.encodeToString(bytes.toByteArray(), 16);

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ivImage.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ivImage.setImageBitmap(bm);
    }

    /**
     * @param parent
     * @param view
     * @param position
     * @param id       Method handles what happens when an item is selected from the spinner
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

} //end class NewItemActivity
