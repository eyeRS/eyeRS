package com.github.eyers.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
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

public class NewItemActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    /**
     * SQL-select query to retrieve the category names.
     */
    public static final String GET_ALL_CATEGORIES =
            "SELECT " + NewCategoryInfo.CategoryInfo.CATEGORY_NAME + " FROM"
                    + NewCategoryInfo.CategoryInfo.TABLE_NAME + ";";
    //db variables
    private static String itemName;
    private static String itemDesc;
    private static String dateAdded;
    /**
     * Category names to be displayed.
     */
    String[] categories = {
            "BOOKS", "CLOTHES", "ACCESSORIES", "GAMES",
            "OTHER", NewCategoryInfo.CategoryInfo.CATEGORY_NAME
    };
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Button btnSelect;
    private ImageView ivImage;
    private String userChoosenTask;
    private SQLiteDatabase db;
    private EyeRSDatabaseHelper eyeRSDatabaseHelper;
    //Fields
    private ImageButton photo;
    private EditText txtTitle;
    private EditText txtDesc;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.photo = (ImageButton) findViewById(R.id.new_item_image);
        this.txtTitle = (EditText) findViewById(R.id.edtTxtItemTitle);
        this.txtDesc = (EditText) findViewById(R.id.edtTxtItemDesc);
        spinner = (Spinner) findViewById(R.id.spinner);

        findViewById(R.id.btnAddItem).setOnClickListener(this);

        // populateSpinner();

        this.spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        position = spinner.getSelectedItemPosition();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );

        this.ivImage = (ImageView) findViewById(R.id.new_item_image);
        this.ivImage.setOnClickListener(this);
    }

    //Open the database connection
    public NewItemActivity open() {
        this.db = eyeRSDatabaseHelper.getWritableDatabase();
        return this;
    }

    //Close the connection
    public void close() {
        eyeRSDatabaseHelper.close();
    }

    //Return all items in the db
    public Cursor getAllCategories() {

        Cursor cursor = db.rawQuery(GET_ALL_CATEGORIES, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    /**
     * Method to populate the spinner.
     */
    public void populateSpinner() {

        //We need to retrieve the categories for the spinner from the db
        try {

            open(); //open db

            //Create the cursor
            Cursor cursor = getAllCategories();

            int[] viewIds = new int[]{R.id.spinner};

            SimpleCursorAdapter cursorAdapter;

            cursorAdapter = new SimpleCursorAdapter(getBaseContext(),
                    R.layout.content_new_item, cursor, categories, viewIds, 0);

            spinner.setAdapter(cursorAdapter);

            close(); //close db

        } catch (SQLiteException ex) {

            Toast.makeText(this, "Unable to view categories", Toast.LENGTH_SHORT).show();

        } finally {
            db.endTransaction();
        }
    }

    /**
     * Method allows us to recently inserted elements from the db.
     */
    @Override
    protected void onResume() {
        super.onResume();

        //   populateSpinner(); //display any new added categories
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
    //Books Category
    private void addBook() {

        ContentValues booksValues = new ContentValues();
        //Book name
        booksValues.put("book_title", itemName);
        //Book description
        booksValues.put("book_desc", itemDesc);
        //Date added
        booksValues.put("date_added", dateAdded);
        //Books icon code inserted here

        try {

            db.beginTransaction();
            //Insert the Book item
            db.insert("BOOKS", dateAdded, booksValues); //Date added may be null

            //Display a message to the user
            Toast.makeText(this, "Your book item has been added successfully ", Toast.LENGTH_LONG).show();
            //Display message in the logcat window after successful operation execution
            Log.e("DATABASE OPERATIONS", "...Book item added to DB!");

        } catch (SQLiteException ex) {
            Toast.makeText(this, "Unable to add item", Toast.LENGTH_SHORT).show();
        } finally {
            db.endTransaction();
        }

    } //end void addBook()

    //Clothes Category
    private void addClothing() {

        ContentValues clothesValues = new ContentValues();
        //Clothing name
        clothesValues.put("clothing_type", itemName);
        //Clothing description
        clothesValues.put("clothing_desc", itemDesc);
        //Date added
        clothesValues.put("date_added", dateAdded);
        //Clothes icon code inserted here

        try {

            db.beginTransaction();
            //Insert the Clothing item
            db.insert("CLOTHES", dateAdded, clothesValues); //Date added may be null

            //Display a message to the user
            Toast.makeText(this, "Your clothing item has been added successfully ", Toast.LENGTH_LONG).show();
            //Display message in the logcat window after successful operation execution
            Log.e("DATABASE OPERATIONS", "...Clothing item added to DB!");

        } catch (SQLiteException ex) {
            Toast.makeText(this, "Unable to add item", Toast.LENGTH_SHORT).show();
        } finally {
            db.endTransaction();
        }

    } //end void addClothing()

    //Accessories Category
    private void addAccessory() {

        ContentValues accessoriesValues = new ContentValues();
        //Accessory name
        accessoriesValues.put("accessory_name", itemName);
        //Accessory description
        accessoriesValues.put("accessory_desc", itemDesc);
        //Date added
        accessoriesValues.put("date_added", dateAdded);
        //Accessory icon code inserted here

        try {

            db.beginTransaction();
            //Insert the Accessory item
            db.insert("ACCESSORIES", dateAdded, accessoriesValues); //Date added may be null

            //Display a message to the user
            Toast.makeText(this, "Your accessory item has been added successfully ", Toast.LENGTH_LONG).show();
            //Display message in the logcat window after successful operation execution
            Log.e("DATABASE OPERATIONS", "...Accessory item added to DB!");

        } catch (SQLiteException ex) {
            Toast.makeText(this, "Unable to add item", Toast.LENGTH_SHORT).show();
        } finally {
            db.endTransaction();
        }

    } //end void addAccessory()

    /**
     * Games Category.
     */
    private void addGame() {

        ContentValues gamesValues = new ContentValues();
        //Default name
        gamesValues.put("game_title", itemName);
        //Default description
        gamesValues.put("game_desc", itemDesc);
        //Date added
        gamesValues.put("date_added", dateAdded);
        //Games icon code inserted here

        try {

            db.beginTransaction();
            //Create the Games default category in the DB
            db.insert("GAMES", dateAdded, gamesValues); //Date added may be null

            //Display a message to the user
            Toast.makeText(this, "Your gaming item has been added successfully ", Toast.LENGTH_LONG).show();
            //Display message in the logcat window after successful operation execution
            Log.e("DATABASE OPERATIONS", "...Game item added to DB!");

        } catch (SQLiteException ex) {
            Toast.makeText(this, "Unable to add item", Toast.LENGTH_SHORT).show();
        } finally {
            db.endTransaction();
        }

    } //end void addGame()

    /**
     * Other Category.
     */
    private void addOther() {

        ContentValues otherValues = new ContentValues();
        //Default name
        otherValues.put("other_title", itemName);
        //Default description
        otherValues.put("other_desc", itemDesc);
        //Date added
        otherValues.put("date_added", dateAdded);
        //Other icon code inserted here

        try {

            db.beginTransaction();
            //Create the Other default category in the DB
            db.insert("OTHER", dateAdded, otherValues); //Date added may be null

            //Display a message to the user
            Toast.makeText(this, "Your other item has been added successfully ", Toast.LENGTH_LONG).show();
            //Display message in the logcat window after successful operation execution
            Log.e("DATABASE OPERATIONS", "...Other item added to DB!");

        } catch (SQLiteException ex) {
            Toast.makeText(this, "Unable to add item", Toast.LENGTH_SHORT).show();
        } finally {
            db.endTransaction();
        }

    } //end void addOther()

    /**
     * Method to add a new Item.
     */
    public void addItemInfo() {

        ContentValues contentValues = new ContentValues();
        //insert the item's name
        contentValues.put(NewItemInfo.ItemInfo.ITEM_NAME, itemName);
        //insert the item's description
        contentValues.put(NewItemInfo.ItemInfo.ITEM_DESC, itemDesc);
        //insert the date the item is added
        contentValues.put(NewItemInfo.ItemInfo.DATE_ADDED, dateAdded);
        //code to insert the item's image to be inserted here

        try {

            db.beginTransaction();
            db.insert(NewItemInfo.ItemInfo.TABLE_NAME, dateAdded, contentValues); //Date added may be null

            //Display a message to the user
            Toast.makeText(this, "Your item has been added successfully ", Toast.LENGTH_LONG).show();
            //Display message in the logcat window after successful operation execution
            Log.e("DATABASE OPERATIONS", "...New item added to DB!");

        } catch (SQLiteException ex) {
            Toast.makeText(this, "Unable to add item", Toast.LENGTH_SHORT).show();
        } finally {
            db.endTransaction();
        }

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
}
