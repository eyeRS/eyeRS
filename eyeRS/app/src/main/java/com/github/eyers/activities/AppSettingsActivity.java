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
 * This class will contain a list of settings in which the user can
 * use to navigate to various preferred app settings.
 */
public class AppSettingsActivity extends AppCompatActivity implements OnItemClickListener {

    static boolean hasRun = false;

    /**
     * Field & other declarations
     */
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_app_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.list_settings);
        listView.setOnItemClickListener(this);

        // Utils.changeToTheme(this, Utils.AppTheme_Red);
    }

    /**
     * @param parent
     * @param view
     * @param position
     * @param id       Method handles what happens when an item is clicked from the list view
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        try {

            if (position == 0) { //Security settings is the first item in the list view i.e. position 0
                Intent intent = new Intent(this, SetPINActivity.class);
                startActivity(intent);
            }
            if (position == 1) { //Display settings is the next item in the list view i.e. position 1
                Intent intent = new Intent(this, DisplaySettingsActivity.class);
                startActivity(intent);
            }
            if (position == 2) { //Profile settings is the next item in the list view i.e. position 2
                Intent intent = new Intent(this, ProfileSettingsActivity.class);
                startActivity(intent);
            }
            if (position == 3) { //Sound settings is the next item in the list view i.e. position 3
                Intent intent = new Intent(this, SoundSettings.class);
                startActivity(intent);
            }
            if (position == 4) { //Category management settings is the next item in the list view i.e. position 4
                Intent intent = new Intent(this, CategoryManagementSettings.class);
                startActivity(intent);
            }
            if (position == 5) { //Item management settings is the next item in the list view i.e. position 5
                Intent intent = new Intent(this, ItemManagementSettings.class);
                startActivity(intent);
            }
            if (position == 6) { //Help & tips is the next item in the list view i.e. position 6
                Intent intent = new Intent(this, HelpActivity.class);
                startActivity(intent);
            }
            if (position == 7) { //About is the next item in the list view i.e. position 7
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
            }

        } catch (Exception ex) {

            Log.e("AppSettings list view", ex.getMessage(), ex);
        }
    }

} //end class AppSettingsActivity
