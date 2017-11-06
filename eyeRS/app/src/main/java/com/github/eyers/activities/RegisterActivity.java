package com.github.eyers.activities;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import com.github.eyers.activities.settings.SettingUtilities;
import com.github.eyers.info.UserRegistrationInfo;

import java.util.regex.Pattern;

/**
 * This class will handle the user registration activity process on first time use of the app.
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
            "In what month was your father born?",
            "Which city does your nearest sibling stay?",
            "Where was your first full time job?",
            "What are the last 4 digits of your ID number?",
            "What time of the day were you born (hh:mm:ss)?"
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

    /**
     * Content resolver declaration
     */
    private ContentResolver eyeRSContentResolver;
    /**
     * Email address validation pattern
     */
    private Pattern regexPattern = Pattern.compile("^[(a-zA-Z-0-9-\\ \\+\\.)]+@[(a-z-A-z)]+\\.[(a-zA-z)]{2,3}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SettingUtilities.onActivityCreateSetTheme(this);
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
            /*
             * Retrieve the saved state of the spinner before the app was destroyed
             */
            spinner.setSelection(savedInstanceState.getInt("spinner"));
        }
    }

    /**
     * Method to add user's Registration details.
     */
    private void addRegInfo() {

        /*
         * Content Resolver declaration
         */
        eyeRSContentResolver = this.getContentResolver();

        try {

            /*
             * Define an object to contain the new values to insert
             */
            ContentValues userRegValues = new ContentValues();

            userRegValues.put(UserRegistrationInfo.USER_NAME, username); //User's name
            userRegValues.put(UserRegistrationInfo.EMAIL_ADD, email); //User's email address
            userRegValues.put(UserRegistrationInfo.USER_PIN, matchedPIN); //User's pin
            userRegValues.put(UserRegistrationInfo.SECURITY_QUESTION, securityQuestion); //User's security question
            userRegValues.put(UserRegistrationInfo.SECURITY_RESPONSE, securityResponse); //User's security response

            /*
             * Content resolver insert operation
             */
            eyeRSContentResolver.insert(
                    DBOperations.CONTENT_URI_USER_REG,
                    userRegValues);

            Toast.makeText(this, "Your details have been saved successfully", Toast.LENGTH_LONG).show();
            Log.e("DATABASE OPERATIONS", "...New user added to DB!");

            /*
             * Then clear the fields
             */
            this.txtUsername.setText("");
            this.txtEmail.setText("");
            this.txtPIN1.setText("");
            this.txtPIN2.setText("");
            this.txtResponse.setText("");

            /*
             * Navigate to the Login screen once registration has been successful
             */
            super.startActivity(new Intent(this, LoginActivity.class));

        } catch (Exception ex) {

            Log.e("Registration insert", ex.getMessage(), ex);
            Toast.makeText(this, "Unable to add details", Toast.LENGTH_SHORT).show();
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

        /*
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

        try {

            switch (view.getId()) {
                case R.id.btnRegister: //user clicks the register button

                    /*
                     * Retrieve user input from fields
                     */
                    username = txtUsername.getText().toString();
                    email = txtEmail.getText().toString();
                    securityResponse = txtResponse.getText().toString();
                    String pinA = txtPIN1.getText().toString();
                    String pinB = txtPIN2.getText().toString();

                    /*
                     * Empty username
                     */
                    if (username.isEmpty()) {

                        Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    /*
                     * Empty email
                     */
                    else if (email.isEmpty()) {

                        Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    /*
                     * Validate email
                     */
                    else if (!validateEmailAddress(email)) {

                        Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    /*
                     * PIN 1 missing
                     */
                    else if (pinA.isEmpty()) {

                        Toast.makeText(this, "Please enter a new PIN", Toast.LENGTH_LONG).show();
                        return;
                    }
                    /*
                     * PIN 2 missing
                     */
                    else if (pinB.isEmpty()) {

                        Toast.makeText(this, "Please verify the new PIN", Toast.LENGTH_LONG).show();
                        return;
                    }
                    /*
                     * PINs do not match
                     */
                    else if (!pinA.equals(pinB)) {

                        Toast.makeText(this, "Sorry but your PINs do not match", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    /*
                     * Length of the PINs
                     */
                    else if ((pinA.length() < 4) && (pinB.length() < 4)) {

                        Toast.makeText(this, "Please ensure your PIN is at least 4 digits",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    /*
                     * Empty security question
                     */
                    else if (securityQuestion.isEmpty()) {

                        Toast.makeText(this, "Please select a security question from the drop-down list",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    /*
                     * Empty security response
                     */
                    else if (securityResponse.isEmpty()) {

                        Toast.makeText(this, "Please enter a response to your security question", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    /*
                     * PINs match
                     */
                    else if (pinA.equals(pinB)) {

                        matchedPIN = txtPIN2.getText().toString(); //get a copy
                    }
                    /*
                     * User input validated
                     */
                    addRegInfo(); //add registration info
                    break;
                case R.id.btnClearReg: //user clicks the clear button

                    this.txtUsername.setText("");
                    this.txtEmail.setText("");
                    this.txtPIN1.setText("");
                    this.txtPIN2.setText("");
                    this.txtResponse.setText("");
                    break;
            }
        } catch (Exception ex) {

            Log.e("Registration validation", ex.getMessage(), ex);
        }

    }

    /**
     * @param emailAddress
     * @return
     */
    public boolean validateEmailAddress(String emailAddress) {
//        return regexPattern.matcher(emailAddress).matches();
        return true;
    }

    /**
     * Save the state of the spinner if it's about to be destroyed.
     *
     * @param savedInstanceState
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        /*
         * Save the selection of the spinner
         */
        savedInstanceState.putInt("spinner", spinner.getSelectedItemPosition());

    }

    /**
     * Method allows us to save the activity's selections just before the app gets paused.
     */
    public void onPause() {

        super.onPause();

        try {

            /*
             * Save the spinner's selection
             */
            spinner = (Spinner) findViewById(R.id.register_spinner);
            SharedPreferences category_prefs = getSharedPreferences("category_prefs", Context.MODE_PRIVATE);
            category_prefs.edit().putInt("spinner_indx", spinner.getSelectedItemPosition()).apply();

        } catch (Exception ex) {

            Log.e("Spinner onPause", ex.getMessage(), ex);
        }

    }

    /**
     * Method allows us to retrieve previous selection before the activity was paused.
     */
    @Override
    protected void onResume() {

        super.onResume();

        try {

            /*
             * Retrieve the saved spinner selection
             */
            spinner = (Spinner) findViewById(R.id.register_spinner);
            SharedPreferences category_prefs = getSharedPreferences("category_prefs", Context.MODE_PRIVATE);
            int spinner_index = category_prefs.getInt("spinner_indx", 0);
            spinner.setSelection(spinner_index);

        } catch (Exception ex) {

            Log.e("Spinner onResume", ex.getMessage(), ex);
        }

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
} //end class RegisterActivity
