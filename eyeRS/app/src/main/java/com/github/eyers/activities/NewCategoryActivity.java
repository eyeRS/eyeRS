package com.github.eyers.activities;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.eyers.DBOperations;
import com.github.eyers.R;
import com.github.eyers.info.NewCategoryInfo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class creates a new category and inserts it into the SQLite database.
 */
public class NewCategoryActivity extends AppCompatActivity implements View.OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemSelectedListener {


    // Fields & other declarations
    private EditText txtTitle;
    private String categoryName;
    private EditText txtDesc;
    private String categoryDesc;
    private String categoryIcon;
    private Spinner iconSpinner;
    private ImageView imageView;
    private HashMap<String, Integer> data;
    /**
     * Content Resolver declaration.
     */
    private ContentResolver eyeRSContentResolver;

    /**
     * Constructor
     */
    public NewCategoryActivity() {
       this. data = new HashMap<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.txtTitle = (EditText) findViewById(R.id.edtTxtCatTitle);
        this.txtDesc = (EditText) findViewById(R.id.edtTxtCatDesc);
        this.iconSpinner = (Spinner) findViewById(R.id.iconSpinner);
        this.iconSpinner.setOnItemSelectedListener(this);
        this.imageView = (ImageView) findViewById(R.id.new_category_image);

        findViewById(R.id.btnAddCategory).setOnClickListener(this);

        populateSpinner();

        /**
         * Retrieve the saved state values before the activity was destroyed
         */
        if (savedInstanceState != null) {

            iconSpinner.setSelection(savedInstanceState.getInt("icon_spinner"));
            txtTitle.setText(savedInstanceState.getString("category_name"));
            txtDesc.setText(savedInstanceState.getString("category_desc"));
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btnAddCategory: //user clicks add
                /**
                 * User cannot add a new category without a title & description
                 */
                validateCategories();
                startActivity(new Intent(this, MainActivity.class));
                break;
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
        //Display the icon selected for the new category
        this.imageView.setImageDrawable(getResources().getDrawable(data.get(iconSpinner.getSelectedItem())));
        //Get a string reference to store into the db
        categoryIcon = parent.getItemAtPosition(position).toString();

    }

    /**
     * Method performs validation to ensure that the user cannot re-create an existing category.
     */
    public void validateCategories() {

        /**
         * Get the text input from the EditText fields
         */
        categoryName = txtTitle.getText().toString();
        categoryDesc = txtDesc.getText().toString();

        /**
         * Content resolver object
         */
        eyeRSContentResolver = this.getContentResolver();

        /**
         * Array of columns to be included for each row retrieved
         */
        String[] projection = {
                NewCategoryInfo.CategoryInfo.CATEGORY_ID,
                NewCategoryInfo.CategoryInfo.CATEGORY_NAME,
                NewCategoryInfo.CategoryInfo.CATEGORY_DESC,
                NewCategoryInfo.CategoryInfo.CATEGORY_ICON};

        /**
         * Cursor object to retrieve query results
         */
        Cursor cursor = eyeRSContentResolver.query(DBOperations.CONTENT_URI_CATEGORIES,
                projection, null, null,
                null);

        if (cursor.moveToFirst()) {

            do {

                /**
                 * If the user tries to create an existing category display appropriate message
                 */
                if (cursor.getString(cursor.getColumnIndex(NewCategoryInfo.
                        CategoryInfo.CATEGORY_NAME)).equals(categoryName)) {

                    Toast.makeText(this, "This category already exists. " +
                            "Please select a unique name for your new category", Toast.LENGTH_SHORT).show();

                    /**
                     * Clear the fields to allow the user to re-enter details
                     */
                    this.txtTitle.setText("");
                    this.txtDesc.setText("");

                }
                /**
                 * If the user is creating a new category
                 */
                if (!cursor.getString(cursor.getColumnIndex(NewCategoryInfo.
                        CategoryInfo.CATEGORY_NAME)).equals(categoryName)) {

                    addNewCategory(); //method to add the new category
                }

            } while (cursor.moveToNext());
        }

        cursor.close();

    } //end void validateCategories()

    /**
     * Method adds the new category to the db only if it has passed the validation test.
     */
    public void addNewCategory() {

        /**
         * Define an object to contain the new values to insert
         */
        ContentValues categoryValues = new ContentValues();

        categoryValues.put(NewCategoryInfo.CategoryInfo.CATEGORY_NAME, categoryName.toUpperCase()); //Category's name
        categoryValues.put(NewCategoryInfo.CategoryInfo.CATEGORY_DESC, categoryDesc); //Category's description
        categoryValues.put(NewCategoryInfo.CategoryInfo.CATEGORY_ICON, categoryIcon); //Icon

        try {

            /**
             * Content resolver insert operation
             */
            eyeRSContentResolver.insert(DBOperations.CONTENT_URI_CATEGORIES, categoryValues);

            Toast.makeText(this, "Your new category has been created successfully ",
                    Toast.LENGTH_SHORT).show();

            /**
             * Clear the text fields
             */
            this.txtTitle.setText("");
            this.txtDesc.setText("");

            Log.e("DATABASE OPERATIONS", "...New category added to DB!");
        } catch (SQLException ex) {
            Toast.makeText(this, "Unable to create category", Toast.LENGTH_SHORT).show();
        }

    } //end void addNewCategory()

    /**
     * A callback method invoked by the loader when initLoader() is called.
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

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    /**
     * Method handles what happens when nothing is selected from the spinner.
     *
     * @param parent
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * Method to populate the spinner.
     */
    public void populateSpinner() {

        for (Field field : R.drawable.class.getDeclaredFields()) {
            String str = field.toString();
            if (str.contains(" int ")) {
                str = str.split(" int ")[1];
                if (str.contains("drawable.ic_")) {
                    try {
                        str = str.split("drawable.")[1].replaceAll("ic_", " ").replaceAll("_", " ").trim();

                        String cap = "";
                        for (int i = 0; i < str.length(); i++) {
                            char x = str.charAt(i);
                            if (x == ' ') {
                                cap = cap + " ";
                                char y = str.charAt(i + 1);
                                cap = cap + Character.toUpperCase(y);
                                i++;
                            } else {
                                cap = cap + x;
                            }
                        }

                        this.data.put(cap.replaceAll(" 24dp", ""), field.getInt(null));
                    } catch (Exception ex) {
                    }
                }
            }
        }

        ArrayList<String> list = new ArrayList<>(data.keySet().size());
        for (String str : data.keySet()) {
            list.add(str);
        }

        //Create an adapter for the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);

        //Set the adapter to the spinner
        this.iconSpinner.setAdapter(adapter);
    }

    /**
     * @param savedInstanceState Save the state of the activity if it's about to be destroyed
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putInt("icon_spinner", iconSpinner.getSelectedItemPosition());
        savedInstanceState.putString("category_name", txtTitle.getText().toString());
        savedInstanceState.putString("category_desc", txtDesc.getText().toString());
    }

} //end class NewCategoryActivity

