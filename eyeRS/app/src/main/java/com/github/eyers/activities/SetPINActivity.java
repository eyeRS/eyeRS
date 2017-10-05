package com.github.eyers.activities;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.eyers.EyeRSDatabaseHelper;
import com.github.eyers.R;

/**
 * This class will handle the PIN reset activity.
 */
public class SetPINActivity extends AppCompatActivity implements View.OnClickListener,
    OnItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String[] QUESTIONS = {

            "What is the name of your junior school?",
            "What is the name of your first pet?",
            "In what year was your father born?",
            "What city does your nearest sibling stay?",
            "What city or town was your first full time job?",
            "What are the last 5 digits of your ID number?",
            "What time of the day were you born (hh:mm)?"
    };

    //db variables
    private static String matchedPIN; // retrieves the PIN as a String if they match
    private static String securityResponse; //retrieves the security response as a String value
    private static String username; //retrieves the username as a String value
    public SQLiteDatabase db;
    private EyeRSDatabaseHelper eyeRSDatabaseHelper;

    /**
     * SQL Update-statement for PIN & Security Response.
     */
    public static final String UPDATE_CREDENTIALS =
            "UPDATE TABLE " + NewRegInfo.UserRegistrationInfo.TABLE_NAME
                    + " SET "
                    + NewRegInfo.UserRegistrationInfo.USER_PIN + " ="
                    + matchedPIN + ", "
                    + NewRegInfo.UserRegistrationInfo.SECURITY_RESPONSE + " ="
                    + securityResponse + " WHERE "
                    + NewRegInfo.UserRegistrationInfo.USER_NAME + " ="
                    + username + ";";

    //Fields
    private EditText txtPIN1;
    private EditText txtPIN2;
    private EditText txtUsername;
    private EditText txtResponse; //retrieves the user's security response
    private Spinner spinner; //contains the list of security questions

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_set_pin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.txtPIN1 = (EditText) findViewById(R.id.txtPIN1);
        this.txtPIN2 = (EditText) findViewById(R.id.txtPIN2);
        this.txtResponse = (EditText) findViewById(R.id.txtSecurityResponse);
        this.txtUsername = (EditText) findViewById(R.id.verifyTxtUsername);

        this.spinner = (Spinner) findViewById(R.id.setPin_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, QUESTIONS); //Populates the spinner with the array contents

        this.spinner.setAdapter(adapter);
        this.spinner.setOnItemSelectedListener(this);

        findViewById(R.id.btnResetPIN).setOnClickListener(this);
        findViewById(R.id.btnClearPIN).setOnClickListener(this);

        if (savedInstanceState != null) {
            /**
             * Retrieve the saved state of the spinner before the app was destroyed
             */
            spinner.setSelection(savedInstanceState.getInt("spinner"));
        }

    }

    /**
     * Open the database connection.
     *
     * @return
     */
    public SetPINActivity open() {
        db = eyeRSDatabaseHelper.getWritableDatabase();
        return this;
    }

    //Close the connection
    public void close() {
        eyeRSDatabaseHelper.close();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnClearPIN:
                this.txtUsername.setText("");
                this.txtPIN1.setText("");
                this.txtPIN2.setText("");
                this.txtResponse.setText("");
                return;
            case R.id.btnResetPIN:
                String pinA = txtPIN1.getText().toString();
                String pinB = txtPIN2.getText().toString();
                String answer = txtResponse.getText().toString();

                //Retrieve the other EditText values for the SQLite update
                securityResponse = txtResponse.getText().toString();
                username = txtUsername.getText().toString();

                if (pinA == null || pinA.equals("")) {
                    Toast.makeText(this, "Please enter a PIN.", Toast.LENGTH_LONG).show();
                    return;
                } else if (pinA == null || pinA.equals("")) {
                    Toast.makeText(this, "Please confirm you PIN.", Toast.LENGTH_LONG).show();
                    return;
                } else if (!pinA.equals(pinB)) {
                    Toast.makeText(this, "Your PINs do not match.", Toast.LENGTH_LONG).show();
                    return;
                } else if (answer == null || answer.equals("")) {
                    Toast.makeText(this, "Please enter a security question.", Toast.LENGTH_LONG).show();
                    return;
                } else if (pinA.equals(pinB)) { //If PINs match then get a copy for the db
                    matchedPIN = txtPIN2.getText().toString();

                    //Update the details
                    updateLoginInfo();
                }

                super.startActivity(new Intent(this, MainActivity.class));
        }
    }

    /**
     * Method to add the updates for the user's login credentials to the db.
     */
    public void updateLoginInfo() {

        //Database handler
        eyeRSDatabaseHelper = new EyeRSDatabaseHelper(getApplicationContext());

        try {

            open(); //open the db connection

            db.beginTransaction();
            db.execSQL(UPDATE_CREDENTIALS); //Updates the credentials

            Toast.makeText(this, "Your credentials have been updated successfully ",
                    Toast.LENGTH_SHORT).show();

            //Display message in the logcat window after successful operation execution
            Log.e("DATABASE OPERATIONS", "...Credentials updated successfully!");

        } catch (SQLException ex) {
            Toast.makeText(this, "Unable to add item", Toast.LENGTH_SHORT).show();
        } finally {
            db.endTransaction();
        }

        close(); //close the db connection
    }

    /**
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     * Method handles what happens when an item is selected from the spinner
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    /**
     * Method handles what happens when nothing is selected from the spinner.
     *
     * @param parent
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * Method allows us to save the activity's selections just before the app gets paused.
     */
    public void onPause() {
        super.onPause();

        //Save the spinner's selection
        spinner = (Spinner)findViewById(R.id.setPin_spinner);
        SharedPreferences category_prefs = getSharedPreferences("category_prefs", Context.MODE_PRIVATE);
        category_prefs.edit().putInt("spinner_indx", spinner.getSelectedItemPosition()).apply();

    }

    /**
     * Method allows us to retrieve previous selection before the activity was paused.
     */
    @Override
    protected void onResume() {
        super.onResume();

        //Retrieve the saved spinner selection
        spinner = (Spinner)findViewById(R.id.setPin_spinner);
        SharedPreferences category_prefs = getSharedPreferences("category_prefs", Context.MODE_PRIVATE);
        int spinner_index = category_prefs.getInt("spinner_indx", 0);
        spinner.setSelection(spinner_index);

    }

    /**
     * Save the state of the spinner if it's about to be destroyed.
     *
     * @param savedInstanceState
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        //save the selection of the spinner
        savedInstanceState.putInt("spinner", spinner.getSelectedItemPosition());

    }

    /** A callback method invoked by the loader when initLoader() is called */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    /** A callback method, invoked after the requested content provider returns all the data */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
