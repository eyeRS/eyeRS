package com.github.eyers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.github.eyers.R;

/**
 * This class will handle item management settings based on the user's preference
 */
public class ItemManagementSettings extends AppCompatActivity implements OnItemClickListener {

    //Declarations
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        switch (position) {
            case 0:
                // Add Item
                startActivity(new Intent(this, NewItemActivity.class));
                break;
            case 1:
                // Edit Item
                break;
            case 2:
                // Delete Item
                break;
            case 3:
                // Change Sorting
                startActivity(new Intent(this, ItemSortingActivity.class));
                break;
        }
    }
}

