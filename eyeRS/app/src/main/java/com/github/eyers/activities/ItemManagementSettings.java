package com.github.eyers.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
     * @param parent
     * @param view
     * @param position
     * @param id       Method handles what happens when an item is clicked from the list view
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (position == 0) { //Add Item
            Intent intent = new Intent(this, NewItemActivity.class);
            startActivity(intent);
        }
        if (position == 1) { //Edit Item

        }
        if (position == 2) { //Delete Item

        }
        if (position == 3) { //Change Sorting
            Intent intent = new Intent(this, ItemSortingActivity.class);
            startActivity(intent);
        }
    }
}

