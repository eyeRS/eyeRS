package com.github.eyers.activities;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.github.eyers.DBOperations;
import com.github.eyers.R;
import com.github.eyers.info.CategoryInfo;

/**
 * This class will handle category management settings events based on the user's selection.
 *
 * @see AppCompatActivity
 * @see OnItemClickListener
 */
public class CategoryManagementSettings extends AppCompatActivity implements OnItemClickListener {

    // Field declarations
    private ListView listView;
    /**
     * Content Resolver declaration.
     */
    private ContentResolver eyeRSContentResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_category_management_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.list_category_mgmt);
        listView.setOnItemClickListener(this);
    }

    /**
     * Method handles what happens when an item is clicked from the list view.
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {

            if (position == 0) { //Add Category
                startActivity(new Intent(this, NewCategoryActivity.class));
            }
            if (position == 1) { //Edit Category
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
            if (position == 2) { //Delete Category
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
            if (position == 3) { //Change Sorting
                startActivity(new Intent(this, CategorySortingActivity.class));
            }

        } catch (Exception ex) {
            Log.e("CategoryManagement", ex.getMessage(), ex);
        }
    }

    private void promptDeletion() {

        /*
         * We need to specify an AlertDialog to prompt the user for deletion
         * to avoid accidental deletion
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(CategoryManagementSettings.this);
        builder.setMessage("Are you sure you want to delete this item? \n" +
                "This operation cannot be undone!")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    /**
                     * User clicks on Ok so delete the item
                     * @param dialog
                     * @param which
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCategory();
                    }
                    /**
                     * User clicks on Cancel so do nothing
                     */
                }).setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Removes the record of the item when the user clicks Ok when prompted for deletion
     * Once an item is deleted it cannot be undone
     */
    private void deleteCategory() {

        /*
         * Content resolver object
         */
        eyeRSContentResolver = this.getContentResolver();

        String[] projection = {
                CategoryInfo.CATEGORY_ID,
                CategoryInfo.CATEGORY_NAME,
                CategoryInfo.CATEGORY_DESC,
                CategoryInfo.CATEGORY_ICON
        };

        String whereClause = "";
        String[] whereArgs = {};
        String sortOrder = CategoryInfo.CATEGORY_NAME;

        String idToDelete = "";

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

            if (cursor.moveToFirst()) {

                if (cursor.getString(cursor.getColumnIndex(CategoryInfo.CATEGORY_NAME))
                        .equals("")) {

                    /**
                     * Retrieves the id of the category to be deleted
                     */
                    idToDelete = cursor.getString(cursor.getColumnIndex(CategoryInfo.CATEGORY_ID));

                } else {

                    Log.e("CatMgmtSettings", "Sorry that category doesn't exist");
                }

                cursor.close();
            }
        } catch (Exception ex) {
            Log.e("ViewItemActivity", "Unable to retrieve item details");
        }

        /*
         * To delete a category simply specify the item's ID in the where clause
         */
        String deleteWhereClause = CategoryInfo.CATEGORY_ID + " = ?";
        String[] deleteWhereArgs = {idToDelete};

        /*
         * Content Resolver delete operation
         */
        eyeRSContentResolver.delete(DBOperations.CONTENT_URI_CATEGORIES,
                deleteWhereClause, deleteWhereArgs);

        Toast.makeText(this, "Your item was deleted successfully", Toast.LENGTH_SHORT).show();
        MainActivity.STATE = "main";
        super.startActivity(new Intent(this, MainActivity.class));
        super.finish();
    }

}
