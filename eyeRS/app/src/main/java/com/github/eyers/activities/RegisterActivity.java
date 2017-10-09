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

import com.github.eyers.DbOperations;
import com.github.eyers.EyeRSDatabaseHelper;
import com.github.eyers.R;

import java.util.regex.Pattern;

/**
 * This class will handle the user registration activity process on first time use of the app
 *
 * @see android.view.View.OnClickListener
 * @see android.support.v7.app.AppCompatActivity
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener,
        OnItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * The possible security questions.
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
     * Field & other declarations
     */
    private static String username;
    private static String email;
    private static String matchedPIN;
    private static String securityQuestion;
    private static String securityResponse;
    private EditText txtUsername;
    private EditText txtEmail;
    private EditText txtPIN1;
    private EditText txtPIN2;
    private EditText txtResponse;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.txtUsername = (EditText) findViewById(R.id.edtTxtUsername);
        this.txtEmail = (EditText) findViewById(R.id.edtTxtEmail);
        this.txtPIN1 = (EditText) findViewById(R.id.edtTxtCreatePIN);
        this.txtPIN2 = (EditText) findViewById(R.id.edtTxtVerifyPIN);
        this.txtResponse = (EditText) findViewById(R.id.edtTxtSecurityResponses);


        this.spinner = (Spinner) findViewById(R.id.register_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, QUESTIONS); // Populates the spinner with the array contents

        this.spinner.setAdapter(adapter);
        this.spinner.setOnItemSelectedListener(this);

        findViewById(R.id.btnRegister).setOnClickListener(this);
        findViewById(R.id.btnClearReg).setOnClickListener(this);

        if (savedInstanceState != null) {
            /**
             * Retrieve the saved state of the spinner before the app was destroyed
             */
            spinner.setSelection(savedInstanceState.getInt("spinner"));
        }
    }

    /**
     *
     */
    private Pattern regexPattern = Pattern.compile("^[(a-zA-Z-0-9-\\ \\+\\.)]+@[(a-z-A-z)]+\\.[(a-zA-z)]{2,3}$");

    /**
     * Method to add user's Registration details.
     */
    public void addRegInfo() {

        /**
         * Content Resolver declaration
         */
        ContentResolver eyeRSContentResolver = this.getContentResolver();

        /**
         * Define an object to contain the new values to insert
         */
        ContentValues userRegValues = new ContentValues();

        userRegValues.put(NewRegInfo.UserRegistrationInfo.USER_NAME, username); //User's name
        userRegValues.put(NewRegInfo.UserRegistrationInfo.EMAIL_ADD, email); //User's email address
        userRegValues.put(NewRegInfo.UserRegistrationInfo.USER_PIN, matchedPIN); //User's pin
        userRegValues.put(NewRegInfo.UserRegistrationInfo.SECURITY_QUESTION, securityQuestion); //User's security question
        userRegValues.put(NewRegInfo.UserRegistrationInfo.SECURITY_RESPONSE, securityResponse); //User's security response

        try {

            /**
             * Content resolver insert operation
             */
            eyeRSContentResolver.insert(DbOperations.CONTENT_URI_USER_REG, userRegValues);

            Toast.makeText(this, "Your details have been saved successfully ", Toast.LENGTH_LONG).show();
            Log.e("DATABASE OPERATIONS", "...New user added to DB!");

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
     * Called when a view has been clicked.
     *
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {

        String pinA = txtPIN1.getText().toString();
        String pinB = txtPIN2.getText().toString();

        switch (view.getId()) {
            case R.id.btnRegister: //user clicks the register button

                username = txtUsername.getText().toString();
                email = txtEmail.getText().toString();
                securityResponse = txtResponse.getText().toString();

                if (!validateEmailAddress(email)) { //email not validate

                    Toast.makeText(this, "Please enter a valid email address.", Toast.LENGTH_LONG).show();
                    return;
                }

                if (pinA.equals(pinB)) { //if pins match

                    matchedPIN = txtPIN2.getText().toString(); //get a copy

                    addRegInfo(); //method to add registration info

                    /**
                     * Navigate to the Login screen once registration has been successful
                     */
                    super.startActivity(new Intent(this, LoginActivity.class));
                }
                return;

            case R.id.btnClearReg: //user clicks the clear button

                this.txtUsername.setText("");
                this.txtEmail.setText("");
                this.txtPIN1.setText("");
                this.txtPIN2.setText("");
                this.txtResponse.setText("");

        }
    }

    /**
     * @param emailAddress
     * @return
     */
    public boolean validateEmailAddress(String emailAddress) {

        return regexPattern.matcher(emailAddress).matches();
    }

    /**
     * @param savedInstanceState Save the state of the spinner if it's about to be destroyed
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        /**
         * Save the selection of the spinner
         */
        savedInstanceState.putInt("spinner", spinner.getSelectedItemPosition());

    }

    /**
     * Method allows us to save the activity's selections just before the app gets paused
     */
    public void onPause() {

        super.onPause();

        /**
         * Save the spinner's selection
         */
        spinner = (Spinner) findViewById(R.id.register_spinner);
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
        spinner = (Spinner) findViewById(R.id.register_spinner);
        SharedPreferences category_prefs = getSharedPreferences("category_prefs", Context.MODE_PRIVATE);
        int spinner_index = category_prefs.getInt("spinner_indx", 0);
        spinner.setSelection(spinner_index);

    }

    /**
     * A callback method invoked by the loader when initLoader() is called
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return null;
    }

    /**
     * A callback method, invoked after the requested content provider returns all the data
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
} //end class RegisterActivity
