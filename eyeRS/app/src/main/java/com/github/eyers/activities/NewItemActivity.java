package com.github.eyers.activities;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.eyers.R;

public class NewItemActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String[] CATEGORIES = {
            "BOOKS", "CLOTHES", "GAMES", "ACCESSORIES", "OTHER"
    };

    //db variables
    private static String itemName;
    private static String itemDesc;
    private static String dateAdded;

    //Fields
    private ImageButton photo;
    private EditText txtTitle;
    private EditText txtDesc;
    private Spinner spinner;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.photo = (ImageButton) findViewById(R.id.new_item_image);
        this.txtTitle = (EditText) findViewById(R.id.edtTxtItemTitle);
        this.txtDesc = (EditText) findViewById(R.id.edtTxtItemDesc);
        this.spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, CATEGORIES); //Populates the spinner with the array contents

        this.spinner.setAdapter(adapter);
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

        findViewById(R.id.btnAddItem).setOnClickListener(this);
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
                        return;
                    }

                }

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
        }
        finally {
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
        }
        finally {
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
        }
        finally {
            db.endTransaction();
        }

    } //end void addAccessory()

    //Games Category
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
        }
        finally {
            db.endTransaction();
        }

    } //end void addGame()

    //Other Category
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
        }
        finally {
            db.endTransaction();
        }

    } //end void addOther()

    //Method to add a new Item
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
        }
        finally {
            db.endTransaction();
        }

    } //end void addItemInfo()
}
