package com.github.eyers.activities;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
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

import com.github.eyers.DBOperations;
import com.github.eyers.R;
import com.github.eyers.info.NewRegInfo;
import com.github.eyers.info.UserRegInfo;

/**
 * This class will handle the PIN reset activity.
 */
public class SetPINActivity extends AppCompatActivity implements View.OnClickListener,
        OnItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Possible security questions
     */
    private static final String[] QUESTIONS = {

            "What is the name of your junior school?",
            "What is the name of your first pet?",
            "In what year was your father born?",
            "What city does your nearest sibling stay?",
            "What city or town was your first full time job?",
            "What are the last 5 digits of your ID number?",
            "What time of the day were you born (hh:mm)?"
    };
    /**
     * Retrieves the username.
     */
    private static String username;
    /**
     * Retrieves the matched pins.
     */
    private static String matchedPIN;
    /**
     * Retrieves the security question.
     */
    private static String securityQuestion;
    /**
     * Retrieves the security response
     */
    private static String securityResponse;
    // Field & other declarations
    private EditText txtPIN1;
    private EditText txtPIN2;
    private EditText txtUsername;
    /**
     * Retrieves the user's security response.
     */
    private EditText txtResponse;
    /**
     * Contains the list of security questions.
     */
    private Spinner spinner;
    /**
     * Content Resolver and db declarations.
     */
    private ContentResolver eyeRSContentResolver;

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
                } else if (pinA.equals(pinB)) { //pins match
                    matchedPIN = txtPIN2.getText().toString();

                    updateLoginInfo(); //method to update details
                }

                super.startActivity(new Intent(this, MainActivity.class));
        }
    }

    /**
     * Method to add the updated user credentials & security details
     */
    public void updateLoginInfo() {

        /**
         * Content resolver object
         */
        eyeRSContentResolver = this.getContentResolver();

        /**
         * Define an object to contain the new values to insert
         */
        ContentValues userRegValues = new ContentValues();

        username = txtUsername.getText().toString();

        /**
         * Array of columns to be included for each row retrieved
         */
        String[] projection = {UserRegInfo.RegInfo.REG_ID,
                UserRegInfo.RegInfo.USER_NAME,
                UserRegInfo.RegInfo.USER_EMAIL,
                UserRegInfo.RegInfo.USER_PIN,
                UserRegInfo.RegInfo.SECURITY_QUESTION,
                UserRegInfo.RegInfo.SECURITY_RESPONSE};

        String where = null;

        /**
         * Cursor object to retrieve query results
         */
        Cursor cursor = eyeRSContentResolver.query(DBOperations.CONTENT_URI_USER_REG,
                projection, null, null,
                null);

        if (cursor != null) {

            if (cursor.getString(1).equals(username)) {

                where = username;
            }
        }

        userRegValues.put(NewRegInfo.UserRegistrationInfo.USER_PIN, matchedPIN); //new matched pin
        userRegValues.put(NewRegInfo.UserRegistrationInfo.SECURITY_QUESTION, securityQuestion); //new security question
        userRegValues.put(NewRegInfo.UserRegistrationInfo.SECURITY_RESPONSE, securityResponse); //new security response

        try {

            eyeRSContentResolver.update(DBOperations.CONTENT_URI_USER_REG, userRegValues,
                    where, null);

            Toast.makeText(this, "Your credentials have been updated successfully ",
                    Toast.LENGTH_SHORT).show();
            Log.e("DATABASE OPERATIONS", "...Credentials updated successfully!");

        } catch (SQLException ex) {
            Toast.makeText(this, "Unable to add item", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Method handles what happens when an item is selected from the spinner.
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        /**
         * Question selected from Spinner
         */
        securityQuestion = parent.getItemAtPosition(position).toString();
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
     * Method allows us to save the activity's selections just before the app gets paused
     */
    public void onPause() {

        super.onPause();

        /**
         * Save the spinner's selection
         */
        spinner = (Spinner) findViewById(R.id.setPin_spinner);
        SharedPreferences category_prefs = getSharedPreferences("category_prefs", Context.MODE_PRIVATE);
        category_prefs.edit().putInt("spinner_indx", spinner.getSelectedItemPosition()).apply();

    }

    /**
     * Method allows us to retrieve previous selection before the activity was paused
     */
    @Override
    protected void onResume() {
        super.onResume();

        /**
         * Retrieve the saved spinner selection
         */
        spinner = (Spinner) findViewById(R.id.setPin_spinner);
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
        /**
         * Save the selection of the spinner
         */
        savedInstanceState.putInt("spinner", spinner.getSelectedItemPosition());

    }

    /**
     * A callback method invoked by the loader when initLoader() is called.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    /**
     * A callback method, invoked after the requested content provider returns all the data.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

} //end class SetPINActivity
