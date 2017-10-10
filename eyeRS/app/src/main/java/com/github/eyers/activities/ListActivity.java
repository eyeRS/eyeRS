package com.github.eyers.activities;

import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.github.eyers.DBOperations;
import com.github.eyers.ItemLabel;
import com.github.eyers.LabelAdapter;
import com.github.eyers.R;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 *
 */
public class ListActivity extends AppCompatActivity
        implements ItemListFragment.ItemListListener, LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Field declarations
     */
    private LabelAdapter adapter;
    private ArrayList<ItemLabel> arrayOfUsers;
    private ListView listView;

    /**
     * Content Resolver declaration
     */
    private ContentResolver eyeRSContentResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        arrayOfUsers = new ArrayList<ItemLabel>();
        adapter = new LabelAdapter(this, arrayOfUsers);

        populateItems();

    }

    /**
     * Method to retrieve the catalog items
     *
     * @return
     */
    public ArrayList<ItemLabel> getAllItems() {

        ArrayList<ItemLabel> addItems = new ArrayList<ItemLabel>();

        /**
         * Content resolver object
         */
        eyeRSContentResolver = this.getContentResolver();

        /**
         * Array of columns to be included for each row retrieved
         */
        String[] projection = {NewItemInfo.ItemInfo.ITEM_ID,
                NewItemInfo.ItemInfo.ITEM_NAME, NewItemInfo.ItemInfo.ITEM_DESC};

        /**
         * The criteria for selecting the rows
         */
        String selection = "item_name = \"" + NewItemInfo.ItemInfo.ITEM_NAME
                + "\"";

        /**
         * Cursor object to retrieve query results
         */
        Cursor cursor = eyeRSContentResolver.query(DBOperations.CONTENT_URI_ITEMS,
                projection, null, null,
                null);

        TreeSet<ItemLabel> data = new TreeSet<>();

        if (cursor.moveToFirst()) {

            do {

//                data.add(new ItemLabel(cursor.getString(1)));

            } while (cursor.moveToNext());

        }

        for (ItemLabel str : data) {

            addItems.add(str);
        }

        return addItems;
    }

    /**
     * Get a specific item
     *
     * @return
     */
    public ItemLabel getItem() {

        /**
         * Array of columns to be included for each row retrieved
         */
        String[] projection = {NewItemInfo.ItemInfo.ITEM_ID,
                NewItemInfo.ItemInfo.ITEM_NAME, NewItemInfo.ItemInfo.ITEM_DESC};

        /**
         * The criteria for selecting the rows
         */
        String selection = "item_name = \"" + NewItemInfo.ItemInfo.ITEM_NAME
                + "\"";

        /**
         * Cursor object to retrieve query results
         */
        Cursor cursor = eyeRSContentResolver.query(DBOperations.CONTENT_URI_ITEMS,
                projection, null, null,
                null);

        ItemLabel item = null;

        if (cursor.moveToFirst()) {

            do {
//                item = new ItemLabel(cursor.getString(1));

            } while (cursor.moveToNext());

        }

        return item;
    }

    //Method to populate the ListView
    public void populateItems() {

        /**
         * Content resolver object
         */
        eyeRSContentResolver = this.getContentResolver();

        try {

            /**
             * Create the cursor
             */
            ArrayList<ItemLabel> items = getAllItems();

            adapter = new LabelAdapter(this, items);

            listView = (ListView) findViewById(R.id.listview);

            listView.setAdapter(adapter);

        } catch (SQLiteException ex) {

            Toast.makeText(this, "Unable to view items", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();

            Log.e("ERROR", "Unable to view items", ex);

        }
    }

    //Method allows us to view recently inserted elements from the db
    @Override
    protected void onResume() {
        super.onResume();

        populateItems(); //display any new items recently added
    }

    @Override
    public void itemClicked(long id) {

        View fragmentContainer = findViewById(R.id.fragment_container);

        if (fragmentContainer != null) { //Only execute if the frame layout is available

            ItemDetailFragment items = new ItemDetailFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction(); //Start the fragment transaction
            items.setItem(id);
            ft.replace(R.id.fragment_container, items);
            ft.addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE); //Get the new and old fragments to fade in and out
            ft.commit(); //Commit the transaction
        } else {

            Intent intent = new Intent(this, ItemDetail.class);
            intent.putExtra(ItemDetail.EXTRA_ITEM_ID, (int) id);
            startActivity(intent);
        }
    }

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

} //end class ListActivity

