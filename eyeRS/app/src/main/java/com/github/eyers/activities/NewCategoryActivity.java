package com.github.eyers.activities;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.eyers.EyeRSDatabaseHelper;
import com.github.eyers.R;

/**
 * This class creates a new category and inserts it into the SQLite database
 */
public class NewCategoryActivity extends AppCompatActivity implements View.OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    //Declarations
    public String categoryName;
    public String categoryDesc;

    //Fields
    private EditText txtTitle;
    private EditText txtDesc;

    //db variables
    public SQLiteDatabase db;
    private EyeRSDatabaseHelper eyeRSDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.txtTitle = (EditText) findViewById(R.id.edtTxtCatTitle);
        this.txtDesc = (EditText) findViewById(R.id.edtTxtCatDesc);

        findViewById(R.id.btnAddCategory).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btnAddCategory: //user clicks add
                //User cannot add a new category without a title & description
                validateCategories();

        }
    }

    /**
     * Open the database connection.
     *
     * @return
     */
    public NewCategoryActivity open() {
        db = eyeRSDatabaseHelper.getWritableDatabase();
        return this;
    }

    /**
     * Close the connection.
     */
    public void close() {
        db.close();
    }

    public final String GET_ALL_CATEGORIES =
            "SELECT " + NewCategoryInfo.CategoryInfo.CATEGORY_NAME + " FROM "
                    + NewCategoryInfo.CategoryInfo.TABLE_NAME + ";";

    /**
     * Method performs validation to ensure that the user cannot re-create an existing category
     */
    public void validateCategories() {

        //Get the text input from the EditText fields
        categoryName = txtTitle.getText().toString();
        categoryDesc = txtDesc.getText().toString();

        //Database handler
        eyeRSDatabaseHelper = new EyeRSDatabaseHelper(getApplicationContext());
        db = eyeRSDatabaseHelper.getReadableDatabase(); //db object

        //Create a cursor to get the data from the db
        Cursor cursor = db.rawQuery(GET_ALL_CATEGORIES, null);

        if (cursor.moveToFirst()) {

            do {

                //If the user tries to create an existing category display appropriate message
                if (cursor.getString(cursor.getColumnIndex(NewCategoryInfo.
                        CategoryInfo.CATEGORY_NAME)).equals(categoryName)) {

                    Toast.makeText(this, "This category already exists. " +
                            "Please select a unique name for your new category", Toast.LENGTH_SHORT).show();

                    //Then clear the fields to allow the user to re-enter details
                    this.txtTitle.setText("");
                    this.txtDesc.setText("");

                }
                //If the user is creating a new category
                if (!cursor.getString(cursor.getColumnIndex(NewCategoryInfo.
                        CategoryInfo.CATEGORY_NAME)).equals(categoryName)) {

                    addNewCategory(); //call the method to add the category
                }

            } while (cursor.moveToNext());
        }

        cursor.close(); //close the cursor
        close(); //close the db connection

    } //end void validateCategories()

    /**
     * Method adds the new category to the db only if it has passed the validation test
     */
    public void addNewCategory() {



        /**
         * Define an object to contain the new values to insert
         */
        ContentValues categoryValues = new ContentValues();

        //Insert the category's name
        categoryValues.put(NewCategoryInfo.CategoryInfo.CATEGORY_NAME, categoryName);
        //Insert the category's description
        categoryValues.put(NewCategoryInfo.CategoryInfo.CATEGORY_DESC, categoryDesc);
        //code to insert the category's icon to be inserted here

        try {

            open();

            db.beginTransaction();

            //insert the category into the db (Category Desc column may be null)


            //Display a message to the user
            Toast.makeText(this, "Your new category has been created successfully ",
                    Toast.LENGTH_SHORT).show();

            //Then clear the text fields
            this.txtTitle.setText("");
            this.txtDesc.setText("");

            //Display message in the logcat window after successful operation execution
            Log.e("DATABASE OPERATIONS", "...New category added to DB!");

        } catch (SQLException ex) {
            Toast.makeText(this, "Unable to create category", Toast.LENGTH_SHORT).show();
        } finally {
            db.endTransaction();
        }

        close(); //close the db connection

    } //end void addNewCategory()

    /**
     * A callback method invoked by the loader when initLoader() is called
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    /**
     * A callback method, invoked after the requested content provider returns all the data
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

} //end class NewCategoryActivity

