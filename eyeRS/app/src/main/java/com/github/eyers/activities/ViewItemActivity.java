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
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.eyers.DBOperations;
import com.github.eyers.R;
import com.github.eyers.info.ItemInfo;
import com.github.eyers.wrapper.ItemWrapper;
import com.vj.widgets.AutoResizeTextView;

public class ViewItemActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * ItemWrapper
     */
    public static ItemWrapper ITEM = null;
    /**
     * Content Resolver declaration.
     */
    private ContentResolver eyeRSContentResolver;

    private ImageView image;
    private AutoResizeTextView title;
    private AutoResizeTextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_view_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.title = (AutoResizeTextView) findViewById(R.id.txtItemTitle);
        this.description = (AutoResizeTextView) findViewById(R.id.txtItemDescription);
        this.image = (ImageView) findViewById(R.id.imgViewItem);

        try {
            this.title.setText(ITEM.getName());
            this.description.setText(ITEM.getDescription());
            this.image.setImageBitmap(ITEM.getImage());
        } catch (NullPointerException npe) {
            Log.e("Error adding item", "Something is null");
        }

        findViewById(R.id.btnEditItem).setOnClickListener(this);
        findViewById(R.id.btnDeleteItem).setOnClickListener(this);

        Toast.makeText(this, ViewItemActivity.ITEM.getName(), Toast.LENGTH_LONG).show();
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
        super.onBackPressed();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnEditItem:
                this.edit();
                break;
            case R.id.btnDeleteItem:
                this.promptDeletion();
                break;
        }
    }

    private void edit() {

        /*
         * Content resolver object
         */
        eyeRSContentResolver = this.getContentResolver();
    }

    private void promptDeletion() {

        /*
         * We need to specify an AlertDialog to prompt the user for deletion
         * to avoid accidental deletion
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewItemActivity.this);
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
                        deleteItem();
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
    private void deleteItem() {

        /*
         * Content resolver object
         */
        eyeRSContentResolver = this.getContentResolver();

        String[] projection = {
                ItemInfo.ITEM_ID,
                ItemInfo.CATEGORY_NAME,
                ItemInfo.ITEM_NAME,
                ItemInfo.ITEM_DESC,
                ItemInfo.ITEM_IMAGE
        };

        String whereClause = "";
        String[] whereArgs = {};
        String sortOrder = ItemInfo.ITEM_NAME;

        String idToDelete = "";

        try {

            /*
             * Content resolver query
             */
            Cursor cursor = eyeRSContentResolver.query(
                    DBOperations.CONTENT_URI_ITEMS,
                    projection,
                    whereClause,
                    whereArgs,
                    sortOrder);

            if (cursor.moveToFirst()) {

                if (cursor.getString(cursor.getColumnIndex(ItemInfo.ITEM_NAME))
                        .equals(this.title.getText().toString())) {

                    /**
                     * Retrieves the id of the item to be deleted
                     */
                    idToDelete = cursor.getString(cursor.getColumnIndex(ItemInfo.ITEM_ID));

                } else {

                    Log.e("ViewItemActivity", "Sorry that item doesn't exist");
                }

                cursor.close();
            }
        } catch (Exception ex) {
            Log.e("ViewItemActivity", "Unable to retrieve item details");
        }

        /*
         * To delete the item simply specify the item's ID in the where clause
         */
        String deleteWhereClause = ItemInfo.ITEM_ID + " = ?";
        String[] deleteWhereArgs = {idToDelete};

        /*
         * Content Resolver delete operation
         */
        eyeRSContentResolver.delete(DBOperations.CONTENT_URI_ITEMS,
                deleteWhereClause, deleteWhereArgs);

        Toast.makeText(this, "Your item was deleted successfully", Toast.LENGTH_SHORT).show();
        MainActivity.STATE = "main";
        super.startActivity(new Intent(this, MainActivity.class));
        super.finish();
    }

} //end class ViewItemActivity
