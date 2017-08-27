package com.github.eyers.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.github.eyers.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    //Set current position to 0 by default
    private int currentPosition = 0;
    //Array of Activity titles
    private String[] titles;

    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawer;
    private ListView drawerList;
    private NavigationView navigationView;

    /*
    //The OnItemClickListener's onItemClick method gets called when the user clicks on an item in the drawer's
    //ListView
    private class DrawerItemClickListener implements ListView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //Code to run when an item gets clicked
            selectItem(position);
            //Set the action bar title
            setActionBarTitle(position);
        }
    }


    //Call the selectItem() method when the user clicks on an item in the drawer's ListView
    private void selectItem(int position){

        //Update the main content by replacing activities
        currentPosition = position;

        //Decide which activity to display based on the position of the item
        //the user selects in the drawer's ListView
        switch(position){

            case 1:
                super.startActivity(new Intent(this, NewItemActivity.class));
                break;
            case 2:

                break;
            case 3:
                super.startActivity(new Intent(this, ViewItemsActivity.class));
                break;
            case 4:
                super.startActivity(new Intent(this, SettingsActivity.class));
                break;
            case 5:

            case 6:
                super.startActivity(new Intent(this, HelpActivity.class));
                break;
            case 7:
                super.startActivity(new Intent(this, AboutActivity.class));
                break;
            default:
                super.startActivity(new Intent(this, MainActivity.class));
        }

        //Set the action bar title
        setActionBarTitle(position);
        //Close drawer
        drawer.closeDrawer(drawerList);
    }
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //titles = getResources().getStringArray(R.array.titles);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        /*
        //Initialise the ListView
        drawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_activated_1, titles));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        //Display the correct activity
        if (savedInstanceState != null){
            currentPosition = savedInstanceState.getInt("position");
            setActionBarTitle(currentPosition);
        }
        //If the activity is newly created, display the MainActivity
        else{
            selectItem(0);
        }
        */

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Create the ActionBarDrawerToggle
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){

            //Called when a drawer has settled in a completely closed state
            public void onDrawerClosed(View view){
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            //Called when a drawer has settled in a completely open state
            public void onDrawerOpened(View drawerView){
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };

        //getActionBar().setDisplayHomeAsUpEnabled(true);
        //getActionBar().setHomeButtonEnabled(true);

        drawer.setDrawerListener(toggle);
    }

    /*
    //Set the action bar title based on the Activity selected
    private void setActionBarTitle(int position) {
        String title;
        if (position == 0){
            //If the user clicks on the Home option, use the app name for the title
            title = getResources().getString(R.string.app_name);
        }
        else{
            //Get the string from the titles array from the position that was clicked and use that
            title = titles[position];
        }

        //Set the action bar title so it reflects the fragment that's displayed
        getActionBar().setTitle(title);
    }
    */

    //Sync the state of the ActionBarDrawerToggle with the state of the drawer
    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    //Pass details of any configuration changes to the ActionBarDrawerToggle
    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    //Called when we call invalidateOptionsMenu()
    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        //If the drawer is open, hide related items to the content view
        boolean drawerOpen = drawer.isDrawerOpen(navigationView);
        //Set the visibility of the menu items when the Drawer is opened or closed
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen); //Hide the action settings when drawer is open
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //Add items in the menu resource file to the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //If the ActionBarDrawerToggle is clicked, let it handle what happens
        if (item.getItemId() == R.id.action_settings){
            super.startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        /*
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        */

        switch(item.getItemId()){
            case R.id.action_settings:
                //Code to run when the Settings action item is clicked
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Method called when the nav_send button is clicked
    public void sendIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "Details of the catalog item to be sent will be attached here");
        String chooserTitle = getString(R.string.chooser); //Get the chooser title
        Intent chosenIntent = Intent.createChooser(intent, chooserTitle);
        startActivity(chosenIntent); //Start the activity that the user selected
    }

    /*
    //Save the state of the current activity if the activity is going to be destroyed
    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt("position", currentPosition);
    }
    */

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_help:
                super.startActivity(new Intent(this, HelpActivity.class));
                break;
            case R.id.nav_new_item:
                super.startActivity(new Intent(this, NewItemActivity.class));
                break;
            case R.id.nav_settings:
                super.startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.nav_about:
                super.startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.nav_slideshow:
                super.startActivity(new Intent(this, SlideshowActivity.class));
                break;

        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }
}
