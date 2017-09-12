package com.github.eyers.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.github.eyers.R;

/**
 * This class will contain a list of settings in which the user can
 * use to navigate to various preferred app settings
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
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     * Method handles what happens when an item is clicked from the list view
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (position == 0) { //General settings is the first item in the list view i.e. position 0
            Intent intent = new Intent();
            startActivity(intent);
        }
        if (position == 1) { //Profile settings is the next item in the list view i.e. position 1
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }
        if (position == 2) { //Category settings is the next item in the list view i.e. position 2
            Intent intent = new Intent();
            startActivity(intent);
        }
        if (position == 3) { //Item Management settings is the next item in the list view i.e. position 3
            Intent intent = new Intent();
            startActivity(intent);
        }
        if (position == 4) { //Help & tips is the next item in the list view i.e. position 4
            Intent intent = new Intent(this, HelpActivity.class);
            startActivity(intent);
        }
        if (position == 5) { //About is the next item in the list view i.e. position 5
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }
    }
}
