package com.github.eyers.activities;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.github.eyers.EyeRSDatabaseHelper;
import com.github.eyers.ItemLabel;
import com.github.eyers.LabelAdapter;
import com.github.eyers.R;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity
        implements ItemListFragment.ItemListListener {

    //SQL-SELECT - Get all the items
    private static String GET_ALL_ITEMS =
            "SELECT " + NewItemInfo.ItemInfo.ITEM_NAME + ", "
                    + NewItemInfo.ItemInfo.ITEM_DESC + ", "
                    + NewItemInfo.ItemInfo.DATE_ADDED + ", "
                    + NewItemInfo.ItemInfo.ITEM_ICON + " FROM "
                    + NewItemInfo.ItemInfo.TABLE_NAME + ";";
    //db variables
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ArrayList<ItemLabel> arrayOfUsers = new ArrayList<ItemLabel>();
        LabelAdapter adapter = new LabelAdapter(this, arrayOfUsers);
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);

        //Create the cursor
        try {
            SQLiteOpenHelper eyersDatabaseHelper = new EyeRSDatabaseHelper(this);
            db = eyersDatabaseHelper.getWritableDatabase();

            //Retrieves the records found in the item table
            Cursor cursor = db.rawQuery(GET_ALL_ITEMS, new String[]{arrayOfUsers.toString()});

            while (cursor.moveToNext()) {
                adapter.add(new ItemLabel(cursor.getString(cursor.getColumnIndex(NewItemInfo.ItemInfo.ITEM_NAME))));
            }
            cursor.close(); //close the cursor

        } catch (SQLiteException ex) {
            Toast.makeText(this, "Unable to view items", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();

            Log.e("ERROR", "Unable to view items", ex);
        }

//        EyeRSDatabaseHelper f = new EyeRSDatabaseHelper();
//    f.getReadableDatabase().beginTransaction();
//        f.getReadableDatabase().qu
//        f.getReadableDatabase().close();
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
}

