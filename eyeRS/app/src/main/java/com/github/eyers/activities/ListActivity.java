package com.github.eyers.activities;

import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.github.eyers.EyeRSDatabaseHelper;
import com.github.eyers.ItemLabel;
import com.github.eyers.LabelAdapter;
import com.github.eyers.R;

import java.util.ArrayList;

/**
 *
 */
public class ListActivity extends AppCompatActivity
        implements ItemListFragment.ItemListListener, LoaderManager.LoaderCallbacks<Cursor> {

    //Declarations
    private LabelAdapter adapter;
    private ArrayList<ItemLabel> arrayOfUsers;
    private ListView listView;

    //SQL-SELECT - Get all the items
    private static String GET_ALL_ITEMS =
            "SELECT " + NewItemInfo.ItemInfo.ITEM_NAME + ", "
                    + NewItemInfo.ItemInfo.ITEM_DESC + ", FROM "
                    + NewItemInfo.ItemInfo.TABLE_NAME + ";";

    //db variables
    private SQLiteDatabase db;
    private EyeRSDatabaseHelper eyeRSDatabaseHelper;

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

    //Open the database connection
    public ListActivity open() {
        db = eyeRSDatabaseHelper.getReadableDatabase();
        return this;
    }

    //Close the connection
    public void close() {
        eyeRSDatabaseHelper.close();
    }

    //Return all items in the db
    public Cursor getAllItems() {

        Cursor cursor = db.rawQuery(GET_ALL_ITEMS, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    //Get a specific item
    public Cursor getItem() {
        String query = "";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    //Method to populate the ListView
    public void populateItems() {

        //Database handler
        eyeRSDatabaseHelper = new EyeRSDatabaseHelper(getApplicationContext());

        open(); //open the db connection

        //Create the cursor and retrieve the items from the db
        try {

            //Create the cursor
            Cursor cursor = getAllItems();

            //Item details to be displayed
            String[] items = new String[]{NewItemInfo.ItemInfo.ITEM_NAME};

            int[] viewIds = new int[]{R.id.lblName};

            SimpleCursorAdapter cursorAdapter;

            cursorAdapter = new SimpleCursorAdapter(getBaseContext(),
                    R.layout.content_list, cursor, items, viewIds, 0);

            listView = (ListView) findViewById(R.id.listview);
            listView.setAdapter(cursorAdapter);

        } catch (SQLiteException ex) {

            Toast.makeText(this, "Unable to view items", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();

            Log.e("ERROR", "Unable to view items", ex);

        } finally {

            db.endTransaction();
        }

        close(); //close the db connection
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

    /** A callback method invoked by the loader when initLoader() is called */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    /** A callback method, invoked after the requested content provider returns all the data */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

