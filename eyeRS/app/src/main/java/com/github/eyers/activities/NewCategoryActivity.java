package com.github.eyers.activities;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.eyers.DBOperations;
import com.github.eyers.R;
import com.github.eyers.activities.settings.SettingUtilities;
import com.github.eyers.info.CategoryInfo;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class creates a new category and inserts it into the SQLite database.
 */
public class NewCategoryActivity extends AppCompatActivity implements View.OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemSelectedListener {


    /* Fields & other declarations */
    private EditText txtTitle;
    private String categoryName;
    private Spinner iconSpinner;
    private ImageView imageView;
    private String categoryIcon;
    private HashMap<String, Integer> data;
    /**
     * Content Resolver declaration.
     */
    private ContentResolver eyeRSContentResolver;

    /**
     * Constructor
     */
    public NewCategoryActivity() {
        this.data = new HashMap<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SettingUtilities.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_new_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.txtTitle = (EditText) findViewById(R.id.edtTxtCatTitle);
        this.iconSpinner = (Spinner) findViewById(R.id.iconSpinner);
        this.iconSpinner.setOnItemSelectedListener(this);
        this.imageView = (ImageView) findViewById(R.id.new_category_image);

        findViewById(R.id.btnAddCat).setOnClickListener(this);

        populateSpinner();

        /*
         * Retrieve the saved state values before the activity was destroyed
         */
        if (savedInstanceState != null) {

            iconSpinner.setSelection(savedInstanceState.getInt("icon_spinner"));
            txtTitle.setText(savedInstanceState.getString("category_name"));
        }
    }

    @Override
    public void onClick(View view) {

        try {
            switch (view.getId()) {

                case R.id.btnAddCat: //user clicks add

                    /*
                     * Retrieve user input from fields
                     */
                    categoryName = txtTitle.getText().toString();

                    /*
                     * Empty category name
                     */
                    if (categoryName.isEmpty()) {

                        Toast.makeText(this, "Please give the category a name",
                                Toast.LENGTH_SHORT).show();
                        break;
                    }
                    /*
                     * No category icon selected
                     */
                    else if (categoryIcon.isEmpty()) {

                        Toast.makeText(this, "Please select an icon for the category",
                                Toast.LENGTH_SHORT).show();
                        break;
                    }

                    validateCategories();
                    break;
            }

        } catch (Exception ex) {

            Log.e("Event handlers", ex.getMessage(), ex);
        }
    }

    /**
     * Method handles what happens when an item is selected from the spinner.
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        //Display the icon selected for the new category
        this.imageView.setImageDrawable(getResources().getDrawable(data.get(iconSpinner.getSelectedItem())));

        imageView.buildDrawingCache();
        Bitmap bmap = imageView.getDrawingCache();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        //Get a string reference to store into the db
        categoryIcon = Base64.encodeToString(bytes.toByteArray(), Base64.DEFAULT);
    }

    /**
     * Method performs validation to ensure that the user cannot re-create an existing category.
     */
    public void validateCategories() {

        /*
         * Content resolver object
         */
        eyeRSContentResolver = this.getContentResolver();

        /*
         * Array of columns to be included for each row retrieved
         */
        String[] projection = {
                CategoryInfo.CATEGORY_ID,
                CategoryInfo.CATEGORY_NAME,
                CategoryInfo.CATEGORY_ICON
        };

        String whereClause = "";
        String[] whereArgs = {};
        String sortOrder = "";

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
                Log.e("NewCategoryActivity", "Null Cursor object");

            } else if (cursor.moveToFirst()) {

                do {

                    /*
                     * If the user tries to create an existing category display appropriate message
                     */
                    if (cursor.getString(cursor.getColumnIndex(CategoryInfo.CATEGORY_NAME)).equals(categoryName)) {

                        Toast.makeText(this, "This category is already in use. " +
                                "Please select a unique name for your new category", Toast.LENGTH_SHORT).show();

                        /*
                         * Clear the fields to allow the user to re-enter details
                         */
                        this.txtTitle.setText("");

                    }
                    /*
                     * If the user is creating a new category
                     */
                    else if (!cursor.getString(cursor.getColumnIndex(CategoryInfo.CATEGORY_NAME)).equals(categoryName)) {

                        addNewCategory(); //method to add the new category
                    }

                } while (cursor.moveToNext());

                cursor.close();

            } else {

                Log.e("NewCategoryActivity", "Cannot retrieve categories");
            }


        } catch (Exception ex) {

            Log.e("Categories query", ex.getMessage(), ex);
        }

    } //end void validateCategories()

    /**
     * Method adds the new category to the db only if it has passed the validation test.
     */
    public void addNewCategory() {

        /*
         * Content resolver object
         */
        eyeRSContentResolver = this.getContentResolver();

        /*
         * Define an object to contain the new values to insert
         */
        ContentValues categoryValues = new ContentValues();

        categoryValues.put(CategoryInfo.CATEGORY_NAME, categoryName.toUpperCase()); //Category's name
        categoryValues.put(CategoryInfo.CATEGORY_ICON, categoryIcon); //Icon

        try {

            /*
             * Content resolver insert operation
             */
            eyeRSContentResolver.insert(
                    DBOperations.CONTENT_URI_CATEGORIES,
                    categoryValues);

            Toast.makeText(this, "Your new category has been created successfully ",
                    Toast.LENGTH_SHORT).show();

            /*
             * Clear the text fields
             */
            this.txtTitle.setText("");

            Log.e("DATABASE OPERATIONS", "...New category added to DB!");

            /*
             *  Then navigate the user to the Home screen after successfully creating the category
             */
            startActivity(new Intent(this, MainActivity.class));

        } catch (Exception ex) {

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

                        Log.e("Spinner population", ex.getMessage(), ex);
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
        MainActivity.STATE = "main";
        super.startActivity(new Intent(this, MainActivity.class));
        super.finish();
    }

} //end class NewCategoryActivity

