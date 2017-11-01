package com.github.eyers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.github.eyers.R;

/**
 * This class will handle item management settings based on the user's preference.
 *
 * @see AppCompatActivity
 * @see OnItemClickListener
 */
public class ItemManagementSettings extends AppCompatActivity implements OnItemClickListener {

    /* Field declarations */
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_item_management_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.list_item_mgmt);
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
            switch (position) {
                case 0: {
                    // Add Item
                    Intent intent = new Intent(this, NewItemActivity.class);
                    startActivity(intent);
                    break;
                }
                case 1: {
                    // Edit Item
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    break;
                }
                case 2: {
                    //Delete Item
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    break;
                }
                case 3: {
                    // Change Sorting
                    Intent intent = new Intent(this, ItemSortingActivity.class);
                    startActivity(intent);
                    break;
                }
            }

        } catch (Exception ex) {

            Log.e("ItemManagement listview", ex.getMessage(), ex);
        }
    }

} //end class ItemManagementSettings

