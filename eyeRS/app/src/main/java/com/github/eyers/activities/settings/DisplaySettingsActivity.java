package com.github.eyers.activities.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.eyers.R;


/**
 * This class will handle display settings events based on the user's preference
 */
public class DisplaySettingsActivity extends AppCompatActivity implements OnClickListener {

    private Spinner fontTypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SettingUtilities.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_display_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String[] fontTypes = getResources().getStringArray(R.array.font_types);

        findViewById(R.id.imgbtn_Blue).setOnClickListener(this);
        findViewById(R.id.imgbtn_Red).setOnClickListener(this);
        findViewById(R.id.imgbtn_Yellow).setOnClickListener(this);
        findViewById(R.id.imgbtn_Green).setOnClickListener(this);

        fontTypeSpinner = (Spinner) findViewById(R.id.spinnerFontType);

        /*
         * Event handler for font type spinner
         */
        fontTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Fonts path
                String Default = "fonts/ArialNarrowFett.ttf";
                String Arial = "fonts/arial.ttf";
                String Antonio = "fonts/Antonio-Bold.ttf";
                String Candy = "fonts/Candy Shop Personal Use.ttf";
                String DroidSans = "fonts/DroidSans-Bold.ttf";

                TextView viewChangeTheme = (TextView) findViewById(R.id.txtView_ChangeTheme);
                TextView ViewChangeFont = (TextView) findViewById(R.id.txtViewChange_Font);


                if (position == 0) {
                    Typeface tf = Typeface.createFromAsset(getAssets(), Default);
                    viewChangeTheme.setTypeface(tf);
                    ViewChangeFont.setTypeface(tf);


                } else if (position == 1) {
                    Typeface tf = Typeface.createFromAsset(getAssets(), Arial);
                    viewChangeTheme.setTypeface(tf);

                } else if (position == 2) {
                    Typeface tf = Typeface.createFromAsset(getAssets(), Antonio);
                    viewChangeTheme.setTypeface(tf);

                } else if (position == 3) {
                    Typeface tf = Typeface.createFromAsset(getAssets(), Candy);
                    viewChangeTheme.setTypeface(tf);

                } else if (position == 4) {
                    Typeface tf = Typeface.createFromAsset(getAssets(), DroidSans);
                    viewChangeTheme.setTypeface(tf);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void onClick(View view) {

        try {

            switch (view.getId()) {

                case R.id.imgbtn_Blue:
                    promptChangeThemeDefault();
                    break;
                case R.id.imgbtn_Red:
                    promptChangeThemeRed();
                    break;
                case R.id.imgbtn_Yellow:
                    promptChangeThemeYellow();
                    break;
                case R.id.imgbtn_Green:
                    promptChangeThemeGreen();
                    break;
            }

        } catch (Exception ex) {

            Log.e("Change Theme", ex.getMessage(), ex);
        }
    }


    private void promptChangeThemeDefault() {

        /*
         * We need to specify an AlertDialog to prompt the user to change the app theme
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(DisplaySettingsActivity.this);
        builder.setMessage("Are you sure you want apply the EyeRS default theme? \n" +
                "Your app will restart and you may lose any unsaved changes")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    /**
                     * User clicks on Ok so change the theme and restart the app
                     * @param dialog
                     * @param which
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changeThemeDefault();
                    }
                    /*
                     * User clicks on Cancel so do nothing
                     */
                }).setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void promptChangeThemeRed() {

        /*
         * We need to specify an AlertDialog to prompt the user to change the app theme
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(DisplaySettingsActivity.this);
        builder.setMessage("Are you sure you want apply the EyeRS Red theme? \n" +
                "Your app will restart and you may lose any unsaved changes")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    /**
                     * User clicks on Ok so change the theme and restart the app
                     * @param dialog
                     * @param which
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changeThemeRed();
                    }
                    /*
                     * User clicks on Cancel so do nothing
                     */
                }).setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void promptChangeThemeYellow() {

        /*
         * We need to specify an AlertDialog to prompt the user to change the app theme
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(DisplaySettingsActivity.this);
        builder.setMessage("Are you sure you want apply the EyeRS Yellow theme? \n" +
                "Your app will restart and you may lose any unsaved changes")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    /**
                     * User clicks on Ok so change the theme and restart the app
                     * @param dialog
                     * @param which
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changeThemeYellow();
                    }
                    /*
                     * User clicks on Cancel so do nothing
                     */
                }).setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void promptChangeThemeGreen() {

        /*
         * We need to specify an AlertDialog to prompt the user to change the app theme
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(DisplaySettingsActivity.this);
        builder.setMessage("Are you sure you want apply the EyeRS Green theme? \n" +
                "Your app will restart and you may lose any unsaved changes")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    /**
                     * User clicks on Ok so change the theme and restart the app
                     * @param dialog
                     * @param which
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changeThemeGreen();
                    }
                    /*
                     * User clicks on Cancel so do nothing
                     */
                }).setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void changeThemeDefault() {

        try {

            SettingUtilities.changeToTheme(this, SettingUtilities.App_Theme);
            Intent intent = getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage(getBaseContext().getPackageName());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        } catch (Exception ex) {

            Log.e("DisplaySettingsActivity", ex.getMessage(), ex);
        }
    }

    private void changeThemeRed() {

        try {

            SettingUtilities.changeToTheme(this, SettingUtilities.AppTheme_Red);
            Intent intent = getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage(getBaseContext().getPackageName());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        } catch (Exception ex) {

            Log.e("DisplaySettingsActivity", ex.getMessage(), ex);
        }
    }

    private void changeThemeYellow() {

        try {

            SettingUtilities.changeToTheme(this, SettingUtilities.AppTheme_Yellow);
            Intent intent = getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage(getBaseContext().getPackageName());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        } catch (Exception ex) {

            Log.e("DisplaySettingsActivity", ex.getMessage(), ex);
        }
    }

    public void changeThemeGreen() {

        try {

            SettingUtilities.changeToTheme(this, SettingUtilities.AppTheme_Green);
            Intent intent = getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage(getBaseContext().getPackageName());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        } catch (Exception ex) {

            Log.e("DisplaySettingsActivity", ex.getMessage(), ex);
        }
    }

} //end class DisplaySettingsActivity




