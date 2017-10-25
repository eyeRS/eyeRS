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
 * This class will handle category management settings events based on the user's selection.
 *
 * @see AppCompatActivity
 * @see OnItemClickListener
 */
public class CategoryManagementSettings extends AppCompatActivity implements OnItemClickListener {

    // Field declarations
    private ListView listView;

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

}
