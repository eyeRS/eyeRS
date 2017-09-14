package com.github.eyers.activities;

import android.content.ContentValues;
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
public class NewCategoryActivity extends AppCompatActivity implements View.OnClickListener {

    //Declarations
    private static String categoryName;
    private static String categoryDesc;

    //Fields
    private EditText txtTitle;
    private EditText txtDesc;

    //db variables
    private SQLiteDatabase db;
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

        categoryName = txtTitle.getText().toString();
        categoryDesc = txtDesc.getText().toString();

        switch (view.getId()) {

            case R.id.btnAddCategory: //user clicks add
                //User cannot add a new category without a title & description
                addCategoryInfo();
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
        eyeRSDatabaseHelper.close();
    }

    /**
     * Method to add a new Category.
     */
    public void addCategoryInfo() {

        //String to query the db for all existing category names
        String existingCategories = "SELECT " + NewCategoryInfo.CategoryInfo.CATEGORY_NAME
                + " FROM " + NewCategoryInfo.CategoryInfo.TABLE_NAME;

        //Database handler
        eyeRSDatabaseHelper = new EyeRSDatabaseHelper(getApplicationContext());

        open(); //open the db connection

        //Create a cursor to get the data from the db
        Cursor cursor = db.rawQuery(existingCategories, null);

        //If the user tries to create an existing category display appropriate message
        if (cursor.getString(1).equals(categoryName)) {

            Toast.makeText(this, "This category already exists. Please select a unique name for your new category",
                    Toast.LENGTH_LONG).show();

            //Then clear the fields to allow the user to re-enter details
            this.txtTitle.setText("");
            this.txtDesc.setText("");

        }
        //If the user is creating a new category
        else if (!cursor.getString(1).equals(categoryName)){

            ContentValues categoryValues = new ContentValues();
            //Insert the category's name
            categoryValues.put(NewCategoryInfo.CategoryInfo.CATEGORY_NAME, categoryName);
            //Insert the category's description
            categoryValues.put(NewCategoryInfo.CategoryInfo.CATEGORY_DESC, categoryDesc);
            //code to insert the category's icon to be inserted here


            try {
                db.beginTransaction();

                //insert the category into the db (Category Desc column may be null)
                db.insert(NewCategoryInfo.CategoryInfo.TABLE_NAME, categoryDesc, categoryValues);

                //Update the db after successfully adding the new category
                EyeRSDatabaseHelper update = new EyeRSDatabaseHelper(this);
                update.updateMyDatabase(db, 1, 1);

                //Display a message to the user
                Toast.makeText(this, "Your new category has been created successfully ", Toast.LENGTH_LONG).show();

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

            cursor.close(); //close the cursor
            close(); //close the db connection
        }

    } //end void addCategoryInfo()
}
