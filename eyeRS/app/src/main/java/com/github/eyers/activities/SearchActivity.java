package com.github.eyers.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.github.eyers.R;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

/**
 *
 */
public class SearchActivity extends AppCompatActivity {
    /**
     * Declare MaterialSearchView when used in activities
     */
    MaterialSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /** From here on copy the code to the necessary activities for functionality of the search
         * function.
         */

        getSupportActionBar().setTitle("Search Bar"); // Sets the name of the menu bar
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF")); // sets the color of the menu bar

        this.searchView = (MaterialSearchView) findViewById(R.id.search_view);

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed()    {

            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener()  {
            @Override
            public boolean onQueryTextSubmit(String query)  {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)    {
                return false;
            }
        });

        /**

         FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
         fab.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        .setAction("Action", null).show();
        }
        });

         getSupportActionBar().setDisplayHomeAsUpEnabled(true);

         */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_bar, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

} //end class SearchActivity
