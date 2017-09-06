package com.github.eyers.activities;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.eyers.R;

public class NewCategoryActivity extends AppCompatActivity implements View.OnClickListener {

    private static String categoryName;
    private static String categoryDesc;

    //Fields
    private EditText txtTitle;
    private EditText txtDesc;

    //db variables
    private SQLiteDatabase db;

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
    public void onClick(View v) {

        categoryName = txtTitle.getText().toString();
        categoryDesc = txtDesc.getText().toString();

        switch (v.getId()) {

            case R.id.btnAddCategory: //user clicks add

                if (txtTitle != null && txtDesc != null) {

                    //User cannot add a new category without a title & description
                    addCategoryInfo();
                } else {

                    Toast.makeText(this, "Please add a Title and a Description to successfully" +
                            "create a new category", Toast.LENGTH_LONG).show();
                }

        }
    }

    //Method to add a new Category
    public void addCategoryInfo() {

        ContentValues categoryValues = new ContentValues();
        //Insert the category's name
        categoryValues.put(NewCategoryInfo.CategoryInfo.CATEGORY_NAME, categoryName);
        //Insert the category's description
        categoryValues.put(NewCategoryInfo.CategoryInfo.CATEGORY_DESC, categoryDesc);
        //code to insert the category's icon to be inserted here


        try {
            db.beginTransaction();

            //insert the category into the db (Category Desc column may be null)
            db.insert(NewCategoryInfo.CategoryInfo.TABLE_NAME, null, categoryValues);

            //Display a message to the user
            Toast.makeText(this, "Your new category has been created successfully ", Toast.LENGTH_LONG).show();
            //Display message in the logcat window after successful operation execution
            Log.e("DATABASE OPERATIONS", "...New category added to DB!");

        } catch (SQLException ex) {
            Toast.makeText(this, "Unable to create category", Toast.LENGTH_SHORT).show();
        }
        finally {
            db.endTransaction();
        }

    } //end void addCategoryInfo()
}
