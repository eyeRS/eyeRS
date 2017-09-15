package com.github.eyers.activities;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.eyers.EyeRSDatabaseHelper;
import com.github.eyers.R;
import com.github.eyers.test.CamTestActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewItemActivity extends AppCompatActivity implements View.OnClickListener,
        OnItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    //db variables
    private static String itemName;
    private static String itemDesc;
    public String category;
    public static ArrayAdapter<String> categoriesAdapter;
    public List<String> addCategories;
    public List<String> popCategories;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private ImageView ivImage;
    private String userChoosenTask;
    public SQLiteDatabase db;
    private EyeRSDatabaseHelper eyeRSDatabaseHelper;
    //Fields
    private EditText txtTitle;
    private EditText txtDesc;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.txtTitle = (EditText) findViewById(R.id.edtTxtItemTitle);
        this.txtDesc = (EditText) findViewById(R.id.edtTxtItemDesc);
        this.spinner = (Spinner) findViewById(R.id.category_spinner);
        this.spinner.setOnItemSelectedListener(this); //spinner click listener

        populateSpinner(this); //load contents of spinner from the db

        findViewById(R.id.btnAddItem).setOnClickListener(this);

        this.ivImage = (ImageView) findViewById(R.id.new_item_image);
        this.ivImage.setOnClickListener(this);

        if (savedInstanceState != null) {
            /**
             * Retrieve the saved state of the spinner before the app was destroyed
             */
            spinner.setSelection(savedInstanceState.getInt("spinner"));
        }
    }

    /**
     * Method to populate the spinner.
     */
    public void populateSpinner(Context context) {

        //Database handler
        eyeRSDatabaseHelper = new EyeRSDatabaseHelper(getApplicationContext());

        //Spinner categories
        popCategories = eyeRSDatabaseHelper.getCategoriesList();

        //Create an adapter for the spinner
        categoriesAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, popCategories);

        //Set the adapter to the spinner
        this.spinner.setAdapter(categoriesAdapter);
    }

    /**
     * Method allows us to save the activity's selections just before the app gets paused
     */
    public void onPause() {

        super.onPause();

        //Save the spinner's selection
        spinner = (Spinner)findViewById(R.id.category_spinner);
        SharedPreferences category_prefs = getSharedPreferences("category_prefs", Context.MODE_PRIVATE);
        category_prefs.edit().putInt("spinner_indx", spinner.getSelectedItemPosition()).apply();

    }

    /**
     * Method allows us to retrieve previous selection before the activity was paused
     */
    @Override
    protected void onResume() {
        super.onResume();

        //Retrieve the saved spinner selection
        spinner = (Spinner)findViewById(R.id.category_spinner);
        SharedPreferences category_prefs = getSharedPreferences("category_prefs", Context.MODE_PRIVATE);
        int spinner_index = category_prefs.getInt("spinner_indx", 0);
        spinner.setSelection(spinner_index);

    }

    //Open the db connection
    public NewItemActivity open() {
        db = eyeRSDatabaseHelper.getWritableDatabase();
        return this;
    }

    //Close the connection
    public void close() {
        db.close();
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
                selectImage();
                if (txtTitle != null) {
                    //if the user is adding a book item
                    if (spinner.getSelectedItem() == "BOOKS") {
                        addBook(); //call the method to add a book item to the db
                        return;
                    }
                    //if the user is adding a clothing item
                    if (spinner.getSelectedItem() == "CLOTHES") {
                        addClothing(); //call the method to add a clothing item to the db
                        return;
                    }
                    //if the user is adding an accessory item
                    if (spinner.getSelectedItem() == "ACCESSORIES") {
                        addAccessory(); //call the method to add an accessory item to the db
                        return;
                    }
                    //if the user is adding a gaming item
                    if (spinner.getSelectedItem() == "GAMES") {
                        addGame(); //call the method to add a gaming item to the db
                        return;
                    }
                    //if the user is adding any other item
                    if (spinner.getSelectedItem() == "OTHER") {
                        addOther(); //call the method to add another item to the db
                        return;
                    }
                    //if the user is adding any item that doesn't correspond to the default categories
                    if (spinner.getSelectedItem() == NewCategoryInfo.CategoryInfo.CATEGORY_NAME) {
                        addItemInfo(); //call the method to add a user specified-item to the db
                    }

                }
                break;
            case R.id.new_item_image:
                selectImage();
                break;
        }

    }

    //DEFAULT CATEGORIES - INSERT operation
    /**
     * Adding a book item
     */
    private void addBook() {

        open(); //open the db connection

        ContentValues booksValues = new ContentValues();
        //Book name
        booksValues.put("book_title", itemName);
        //Book description
        booksValues.put("book_desc", itemDesc);

        try {

            db.beginTransaction();

            //Insert the Book item
            db.insert("BOOKS", itemDesc, booksValues); //Desc may be null

            //Display a message to the user
            Toast.makeText(this, "Your book item has been added successfully ", Toast.LENGTH_LONG).show();
            //Display message in the logcat window after successful operation execution
            Log.e("DATABASE OPERATIONS", "...Book item added to DB!");

        } catch (SQLiteException ex) {
            Toast.makeText(this, "Unable to add item", Toast.LENGTH_SHORT).show();
        } finally {
            db.endTransaction();
        }

        close(); //close the db connection

    } //end void addBook()

    /**
     * Adding a clothing item
     */
    private void addClothing() {

        open(); //open the db connection

        ContentValues clothesValues = new ContentValues();
        //Clothing name
        clothesValues.put("clothing_type", itemName);
        //Clothing description
        clothesValues.put("clothing_desc", itemDesc);

        try {

            db.beginTransaction();

            //Insert the Clothing item
            db.insert("CLOTHES", itemDesc, clothesValues); //Desc may be null

            //Display a message to the user
            Toast.makeText(this, "Your clothing item has been added successfully ", Toast.LENGTH_LONG).show();
            //Display message in the logcat window after successful operation execution
            Log.e("DATABASE OPERATIONS", "...Clothing item added to DB!");

        } catch (SQLiteException ex) {
            Toast.makeText(this, "Unable to add item", Toast.LENGTH_SHORT).show();
        }
        finally {
            db.endTransaction();
        }

        close(); //close the db connection

    } //end void addClothing()

    /**
     * Adding an accessory item
     */
    private void addAccessory() {

        open(); //open the db connection

        ContentValues accessoriesValues = new ContentValues();
        //Accessory name
        accessoriesValues.put("accessory_name", itemName);
        //Accessory description
        accessoriesValues.put("accessory_desc", itemDesc);

        try {

            db.beginTransaction();

            //Insert the Accessory item
            db.insert("ACCESSORIES", itemDesc, accessoriesValues); //Desc may be null

            //Display a message to the user
            Toast.makeText(this, "Your accessory item has been added successfully ", Toast.LENGTH_LONG).show();
            //Display message in the logcat window after successful operation execution
            Log.e("DATABASE OPERATIONS", "...Accessory item added to DB!");

        } catch (SQLiteException ex) {
            Toast.makeText(this, "Unable to add item", Toast.LENGTH_SHORT).show();
        } finally {
            db.endTransaction();
        }

        close(); //close the db connection

    } //end void addAccessory()

    /**
     * Adding a gaming item
     */
    private void addGame() {

        open(); //open the db connection

        ContentValues gamesValues = new ContentValues();
        //Default name
        gamesValues.put("game_title", itemName);
        //Default description
        gamesValues.put("game_desc", itemDesc);

        try {

            db.beginTransaction();

            //Insert the Game item
            db.insert("GAMES", itemDesc, gamesValues); //Desc may be null

            //Display a message to the user
            Toast.makeText(this, "Your gaming item has been added successfully ", Toast.LENGTH_LONG).show();
            //Display message in the logcat window after successful operation execution
            Log.e("DATABASE OPERATIONS", "...Game item added to DB!");

        } catch (SQLiteException ex) {
            Toast.makeText(this, "Unable to add item", Toast.LENGTH_SHORT).show();
        } finally {
            db.endTransaction();
        }

        close(); //close the db connection

    } //end void addGame()

    /**
     * Adding a random item
     */
    private void addOther() {

        open(); //open the db connection

        ContentValues otherValues = new ContentValues();
        //Default name
        otherValues.put("other_title", itemName);
        //Default description
        otherValues.put("other_desc", itemDesc);

        try {

            db.beginTransaction();

            //Insert the Other item
            db.insert("OTHER", itemDesc, otherValues); //Desc may be null

            //Display a message to the user
            Toast.makeText(this, "Your other item has been added successfully ", Toast.LENGTH_LONG).show();
            //Display message in the logcat window after successful operation execution
            Log.e("DATABASE OPERATIONS", "...Other item added to DB!");

        } catch (SQLiteException ex) {
            Toast.makeText(this, "Unable to add item", Toast.LENGTH_SHORT).show();
        } finally {
            db.endTransaction();
        }

        close(); //close the db connection

    } //end void addOther()

    /**
     * Method to add a new Item.
     */
    public void addItemInfo() {

        open(); //open the db connection

        ContentValues contentValues = new ContentValues();
        //insert the item's name
        contentValues.put(NewItemInfo.ItemInfo.ITEM_NAME, itemName);
        //insert the item's description
        contentValues.put(NewItemInfo.ItemInfo.ITEM_DESC, itemDesc);

        try {

            db.beginTransaction();

            db.insert(NewItemInfo.ItemInfo.TABLE_NAME, itemDesc, contentValues); //Desc may be null

            //Display a message to the user
            Toast.makeText(this, "Your item has been added successfully ", Toast.LENGTH_LONG).show();
            //Display message in the logcat window after successful operation execution
            Log.e("DATABASE OPERATIONS", "...New item added to DB!");

        } catch (SQLiteException ex) {
            Toast.makeText(this, "Unable to add item", Toast.LENGTH_SHORT).show();
        } finally {
            db.endTransaction();
        }

        close(); //close the db connection

    } //end void addItemInfo()

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
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
                boolean result = CamTestActivity.checkPermission(NewItemActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

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
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

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
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     * Method handles what happens when an item is selected from the spinner
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        //Category selected from Spinner
        category = parent.getItemAtPosition(position).toString();

    }

    /**
     *
     * @param parent
     * Method handles what happens when nothing is selected from the spinner
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * @param savedInstanceState
     * Save the state of the spinner if it's about to be destroyed
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        //save the selection of the spinner
        savedInstanceState.putInt("spinner", spinner.getSelectedItemPosition());

    }

    /** A callback method invoked by the loader when initLoader() is called */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    /** A callback method, invoked after the requested content provider returns all the data */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
