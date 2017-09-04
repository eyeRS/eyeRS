package com.github.eyers.activities;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.eyers.R;

import java.sql.Blob;

public class NewItemActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String[] CATEGORIES = {
            "BOOKS", "CLOTHES", "GAMES", "ACCESSORIES", "OTHER"
    };

    //Declarations
    private ImageButton photo;
    private EditText txtTitle;
    private EditText txtDesc;
    private Spinner spinner;
    EyeRSDatabaseHelper eyeRSDatabaseHelper;
    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.photo = (ImageButton) findViewById(R.id.new_item_image);
        this.txtTitle = (EditText) findViewById(R.id.edtTxtTitle);
        this.txtDesc = (EditText) findViewById(R.id.edtTxtDescription);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnAddItem: //user clicks add

                if (txtTitle != null ){
                    //User cannot add a new item without a title, description may be null although not advisable
                    if (txtDesc == null){
                        Toast.makeText(this, "You should consider adding a Description for " +
                                "your item", Toast.LENGTH_LONG).show();
                    }
                    //if the user is adding a book item
                    if (spinner.getSelectedItem() == "BOOKS"){

                    }
                    //if the user is adding a clothing item
                    if (spinner.getSelectedItem() == "CLOTHES"){

                    }
                    //if the user is adding an accessory item
                    if (spinner.getSelectedItem() == "ACCESSORIES"){

                    }
                    //if the user is adding a gaming item
                    if (spinner.getSelectedItem() == "GAMES"){

                    }
                    //if the user is adding any other item
                    if (spinner.getSelectedItem() == "OTHER"){

                    }
                    //if the user is adding any item that doesn't correspond to the default categories
                    if (spinner.getSelectedItem() == NewCategoryInfo.CategoryInfo.CATEGORY_NAME){

                    }


                }
                else{
                    Toast.makeText(this, "Please add a Title to successfully" +
                            "add a new item", Toast.LENGTH_LONG).show();
                }

                return;

        }
    }

    //DEFAULT CATEGORIES - INSERT operation
    //Books Category
    private void addBook(SQLiteDatabase db, String itemName,
                         String itemDesc, String dateAdded, Blob item_Icon) {

        ContentValues booksValues = new ContentValues();
        //Book name
        booksValues.put("book_title", itemName);
        //Book description
        booksValues.put("book_desc", itemDesc);
        //Date added
        booksValues.put("date_added", dateAdded);
        //Books icon code inserted here


        //Insert the Book item
        db.insert("BOOKS", "book_desc", booksValues); //Description may be null

    } //end void addBook()

    //Clothes Category
    private void addClothing(SQLiteDatabase db, String itemName,
                             String itemDesc, String dateAdded, Blob item_Icon) {

        ContentValues clothesValues = new ContentValues();
        //Clothing name
        clothesValues.put("clothing_type", itemName);
        //Clothing description
        clothesValues.put("clothing_desc", itemDesc);
        //Date added
        clothesValues.put("date_added", dateAdded);
        //Clothes icon code inserted here


        //Insert the Clothing item
        db.insert("CLOTHES", "clothing_desc", clothesValues); //Description may be null

    } //end void addClothing()

    //Accessories Category
    private void addAccessory(SQLiteDatabase db, String itemName,
                              String itemDesc, String dateAdded, Blob item_Icon) {

        ContentValues accessoriesValues = new ContentValues();
        //Accessory name
        accessoriesValues.put("accessory_name", itemName);
        //Accessory description
        accessoriesValues.put("accessory_desc", itemDesc);
        //Date added
        accessoriesValues.put("date_added", dateAdded);
        //Accessory icon code inserted here


        //Insert the Accessory item
        db.insert("ACCESSORIES", "accessory_desc", accessoriesValues); //Description may be null

    } //end void addAccessory()

    //Games Category
    private void addGame(SQLiteDatabase db, String itemName,
                         String itemDesc, String dateAdded, Blob item_Icon) {

        ContentValues gamesValues = new ContentValues();
        //Default name
        gamesValues.put("game_title", itemName);
        //Default description
        gamesValues.put("game_desc", itemDesc);
        //Date added
        gamesValues.put("date_added", dateAdded);
        //Games icon code inserted here


        //Create the Games default category in the DB
        db.insert("GAMES", "game_desc", gamesValues); //Description may be null

    } //end void addGame()

    //Other Category
    private void addOther(SQLiteDatabase db, String itemName,
                          String itemDesc, String dateAdded, Blob item_Icon) {

        ContentValues otherValues = new ContentValues();
        //Default name
        otherValues.put("other_title", itemName);
        //Default description
        otherValues.put("other_desc", itemDesc);
        //Date added
        otherValues.put("date_added", dateAdded);
        //Other icon code inserted here


        //Create the Other default category in the DB
        db.insert("OTHER", "other_desc", otherValues); //Description may be null

    } //end void addOther()
}
