package com.github.eyers.activities.settings;

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
import com.github.eyers.activities.MainActivity;
import com.github.eyers.activities.NewCategoryActivity;

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
        SettingUtilities.onActivityCreateSetTheme(this);
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
                return;
            } else if (position == 1) { //Edit Category
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return;
            } else if (position == 2) { //Delete Category
                Intent intent = new Intent(this, DeleteCategory.class);
                startActivity(intent);
                return;
            }
        } catch (Exception ex) {
            Log.e("Category Management", "error selecting option", ex);
        }
    }
}
