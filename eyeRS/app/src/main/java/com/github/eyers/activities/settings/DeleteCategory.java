package com.github.eyers.activities.settings;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.github.eyers.DBOperations;
import com.github.eyers.EyeRS;
import com.github.eyers.ItemLabel;
import com.github.eyers.LabelAdapter;
import com.github.eyers.R;
import com.github.eyers.activities.MainActivity;
import com.github.eyers.info.CategoryInfo;

import java.util.ArrayList;

/**
 * DeleteCategory. Matthew: not sure what package this class belongs in.
 *
 * @author Matthew Van der Bijl
 */
public class DeleteCategory extends AppCompatActivity implements AdapterView.OnItemClickListener {

    // Field declarations
    private String name;
    private ListView listView;
    /**
     * Content Resolver declaration.
     */
    private ContentResolver eyeRSContentResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SettingUtilities.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_delete_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.listView = (ListView) findViewById(R.id.listview);
        this.listView.setOnItemClickListener(this);

        ArrayList<ItemLabel> items = new ArrayList<>();
        for (ItemLabel category : EyeRS.getCategoriesList(this)) {
            items.add(category);
        }

        LabelAdapter adapter = new LabelAdapter(this, items);
        listView.setAdapter(adapter);

        Toast.makeText(this, "Please select the item you wish to delete.", Toast.LENGTH_LONG).show();
    }

    private void promptDeleteCategory() {
        /*
         * We need to specify an AlertDialog to prompt the user for deletion
         * to avoid accidental deletion
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(DeleteCategory.this);
        builder.setMessage("Are you sure you want to delete this category? \n" +
                "This operation cannot be undone!")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    /**
                     * User clicks on Ok so delete the category
                     * @param dialog Dialog pop up
                     * @param which
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        validateDeletion();
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
    private void validateDeletion() {

        /*
         * Content resolver object
         */
        eyeRSContentResolver = this.getContentResolver();

        String[] projection = {
                CategoryInfo.CATEGORY_ID,
                CategoryInfo.CATEGORY_NAME,
                CategoryInfo.CATEGORY_ICON
        };

        String whereClause = "";
        String[] whereArgs = {};
        String sortOrder = CategoryInfo.CATEGORY_NAME;

        String categoryToDelete;

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

            if (!cursor.moveToFirst()){

                Toast.makeText(this, "Oops something happened there", Toast.LENGTH_SHORT).show();
                Log.e("DeleteCategory", "Unable to retrieve cursor value");
            }
            else if (cursor.moveToFirst()) {

                do {

                    if (cursor.getString(cursor.getColumnIndex(CategoryInfo.CATEGORY_NAME)).
                            equals(name.toUpperCase())) {

                        /*
                         * Retrieves the id of the category to be deleted
                         */
                        categoryToDelete = cursor.getString(cursor.getColumnIndex(CategoryInfo.CATEGORY_ID));
                        deleteCategory(categoryToDelete);
                    }

                } while (cursor.moveToNext());

                cursor.close();

            } else {

                Log.e("DeleteCategory", "Unable to retrieve category");
            }
        } catch (Exception ex) {

            Log.e("CatMgmtSettings", "Sorry that category doesn't exist");
        }
    }

    /**
     * Method to delete a category
     * @param categoryToDelete
     */
    private void deleteCategory(String categoryToDelete) {

        /*
         * Content resolver object
         */
        eyeRSContentResolver = this.getContentResolver();

        /*
         * To delete a category simply specify the item's ID in the where clause
         */
        String deleteWhereClause = CategoryInfo.CATEGORY_ID + " = ?";
        String[] deleteWhereArgs = {categoryToDelete};

        try {

            /*
             * Content Resolver delete operation
             */
            eyeRSContentResolver.delete(
                    DBOperations.CONTENT_URI_CATEGORIES,
                    deleteWhereClause,
                    deleteWhereArgs);

        } catch (Exception ex) {

            Log.e("DeleteCategory", ex.getMessage(), ex);
        }

        Toast.makeText(this, "Your category was deleted successfully", Toast.LENGTH_SHORT).show();
        Log.e("Deleted item id", deleteWhereClause);
        MainActivity.STATE = "main";
        super.startActivity(new Intent(this, MainActivity.class));
        super.finish();
    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        name = parent.getItemAtPosition(position).toString();

        if (name.toUpperCase().equals("BOOKS")
                || name.toUpperCase().equals("CLOTHES")
                || name.toUpperCase().equals("ACCESSORIES")
                || name.toUpperCase().equals("GAMES")
                || name.toUpperCase().equals("OTHER")){

            Toast.makeText(this, "Sorry default categories cannot be deleted!",
                    Toast.LENGTH_SHORT).show();
            super.startActivity(new Intent(this, ItemManagementSettings.class));

        } else if (!name.toUpperCase().equals("BOOKS")
                || !name.toUpperCase().equals("CLOTHES")
                || !name.toUpperCase().equals("ACCESSORIES")
                || !name.toUpperCase().equals("GAMES")
                || !name.toUpperCase().equals("OTHER")){

            /*
             * Not a default category so proceed to prompt deletion
             */
            promptDeleteCategory();

        }
        else{

            Log.e("Delete Category", "Error deleting category");
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try {
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ECLAIR
                    && keyCode == KeyEvent.KEYCODE_BACK
                    && event.getRepeatCount() == 0) {
                // Take care of calling this method on earlier versions of
                // the platform where it doesn't exist.
                onBackPressed();
            }

        } catch (Exception ex) {
            Log.e("MainActivity key_down", ex.getMessage(), ex);
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        MainActivity.STATE = "main";
        super.startActivity(new Intent(this, MainActivity.class));
        super.finish();
    }
}
