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
 * This class will contain a list of settings in which the user can
 * use to navigate to various preferred app settings.
 */
public class AppSettingsActivity extends AppCompatActivity implements OnItemClickListener {

    //Declarations
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.list_settings);
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
                // Security settings is the first item in the list view i.e. position 0
                startActivity(new Intent(this, SetPINActivity.class));
                break;
            case 1:
                // Display settings is the next item in the list view i.e. position 1
                startActivity(new Intent(this, DisplaySettingsActivity.class));
                break;
            case 2:
                // Profile settings is the next item in the list view i.e. position 2
                startActivity(new Intent(this, ProfileSettings.class));
                break;
            case 3:
                // Sound settings is the next item in the list view i.e. position 3
                startActivity(new Intent(this, SoundSettings.class));
                break;
            case 4:
                // Category management settings is the next item in the list view i.e. position 4
                startActivity(new Intent(this, NewCategoryInfo.CategoryInfo.class));
                break;
            case 5:
                // Item management settings is the next item in the list view i.e. position 5
                startActivity(new Intent(this, ItemManagementSettings.class));
                break;
            case 6:
                // Help & tips is the next item in the list view i.e. position 6
                startActivity(new Intent(this, HelpActivity.class));
                break;
            case 7:
                // About is the next item in the list view i.e. position 7
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }
    }
}
