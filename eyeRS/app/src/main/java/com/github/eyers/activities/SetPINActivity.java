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
import com.github.eyers.Utils;
import com.github.eyers.info.UserRegistrationInfo;

import java.util.regex.Pattern;

/**
 * This class will handle the PIN reset activity.
 */
public class SetPINActivity extends AppCompatActivity implements View.OnClickListener,
        OnItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * The possible security questions.
     */
    private static final String[] QUESTIONS = {

            "What is the name of your junior school?",
            "What is the name of your first pet?",
            "In what year was your father born?",
            "What city does your nearest sibling stay?",
            "What city was your first full time job?",
            "What are the last 5 digits of your ID number?",
            "What time of the day were you born (hh:mm)?"
    };
    /**
     * Retrieves the username.
     */
    private static String username;
    /**
     * Retrieves the email
     */
    private static String email;
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
    /**
     * Email address validation pattern
     */
    private Pattern regexPattern = Pattern.compile("^[(a-zA-Z-0-9-\\ \\+\\.)]+@[(a-z-A-z)]+\\.[(a-zA-z)]{2,3}$");
    /**
     * Field declarations
     */
    private EditText txtPIN1;
    private EditText txtPIN2;
    private EditText txtUsername;
    private EditText txtEmail;
    /**
     * Retrieves the user's security response.
     */
    private EditText txtResponse;
    /**
     * Contains the list of security questions.
     */
    private Spinner spinner;
    /**
     * Content Resolver declarations
     */
    private ContentResolver eyeRSContentResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        super.setContentView(R.layout.activity_set_pin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.txtPIN1 = (EditText) findViewById(R.id.txtNewPIN1);
        this.txtPIN2 = (EditText) findViewById(R.id.txtNewPIN2);
        this.txtResponse = (EditText) findViewById(R.id.txtNewSecurityResponse);
        this.txtUsername = (EditText) findViewById(R.id.verifyTxtUsername);
        this.txtEmail = (EditText) findViewById(R.id.verifyTxtEmail);

        this.spinner = (Spinner) findViewById(R.id.detailsUpdate_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, QUESTIONS); //Populates the spinner with the array contents

        this.spinner.setAdapter(adapter);
        this.spinner.setOnItemSelectedListener(this);

        findViewById(R.id.btnResetPIN).setOnClickListener(this);
        findViewById(R.id.btnClearPIN).setOnClickListener(this);

        if (savedInstanceState != null) {
            /*
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

        try {

            switch (view.getId()) {

                case R.id.btnClearPIN:
                    this.txtUsername.setText("");
                    this.txtPIN1.setText("");
                    this.txtPIN2.setText("");
                    this.txtEmail.setText("");
                    this.txtResponse.setText("");
                    break;
                case R.id.btnResetPIN:

                    username = txtUsername.getText().toString();
                    String pinA = txtPIN1.getText().toString();
                    String pinB = txtPIN2.getText().toString();
                    email = txtEmail.getText().toString();
                    securityResponse = txtResponse.getText().toString();

                    /*
                     * Empty username
                     */
                    if (username.isEmpty()) {

                        Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    /*
                     * Empty email
                     */
                    else if (email.isEmpty()) {

                        Toast.makeText(this, "Please enter a valid email address.", Toast.LENGTH_LONG).show();
                        break;
                    }
                    /*
                     * Email validation
                     */
                    else if (!validateEmailAddress(email)) {

                        Toast.makeText(this, "Please enter a valid email address.", Toast.LENGTH_LONG).show();
                        break;
                    }
                    /*
                     * PIN 1 is empty
                     */
                    else if (pinA.isEmpty()) {

                        Toast.makeText(this, "Please enter a new PIN", Toast.LENGTH_LONG).show();
                        break;
                    }
                    /*
                     * PIN 2 is empty
                     */
                    else if (pinB.isEmpty()) {

                        Toast.makeText(this, "Please confirm your new PIN", Toast.LENGTH_LONG).show();
                        break;
                    }
                    /*
                     * PINs do not match
                     */
                    else if (!pinA.equals(pinB)) {

                        Toast.makeText(this, "Your PINs do not match.", Toast.LENGTH_LONG).show();
                        break;

                    }
                    /*
                     * Empty security question
                     */
                    else if (securityQuestion.isEmpty()) {

                        Toast.makeText(this, "Please select a security question from the drop-down list",
                                Toast.LENGTH_SHORT).show();
                        break;
                    }
                    /*
                     * Empty security response
                     */
                    else if (securityResponse.isEmpty()) {

                        Toast.makeText(this, "Please enter a response for your security question",
                                Toast.LENGTH_SHORT).show();
                        break;
                    }
                    /*
                     * No matching PIN
                     */
                    else if (matchedPIN.isEmpty()) {

                        Toast.makeText(this, "Please ensure you've entered PINs that match",
                                Toast.LENGTH_SHORT).show();
                        break;
                    }
                    /*
                     * PINs match so get a copy
                     */
                    else if (pinA.equals(pinB)) {

                        /*
                         * Get a copy of the matched PINs
                         */
                        matchedPIN = txtPIN2.getText().toString();
                    }

                    updateLoginInfo(); //update the user's details
                    break;
            }

        } catch (Exception ex) {

            Log.e("SetPIN event handlers", ex.getMessage(), ex);
        }
    }

    /**
     * Method to add the updated user credentials & security details
     */
    public void updateLoginInfo() {

        /*
         * Array of columns to be included for each row retrieved
         */
        String[] projection = {UserRegistrationInfo.REG_ID,
                UserRegistrationInfo.USER_NAME,
                UserRegistrationInfo.EMAIL_ADD,
                UserRegistrationInfo.USER_PIN,
                UserRegistrationInfo.SECURITY_QUESTION,
                UserRegistrationInfo.SECURITY_RESPONSE};

        String whereClause = UserRegistrationInfo.SECURITY_QUESTION + " = '" + securityQuestion
                + "' AND " + UserRegistrationInfo.SECURITY_RESPONSE + " = '" + securityResponse + "'";

        String[] whereArgs = {};
        String sortOrder = "";

        String idToUpdate = "";

        /*
         * Content resolver object
         */
        eyeRSContentResolver = this.getContentResolver();

        /*
         * Cursor object to retrieve query results
         */
        Cursor cursor = eyeRSContentResolver.query(DBOperations.CONTENT_URI_USER_REG,
                projection, whereClause, whereArgs,
                sortOrder);

        if (!cursor.moveToFirst()) {

            Toast.makeText(this, "Oops something happened there!", Toast.LENGTH_SHORT).show();
            Log.e("SetPINActivity", "Null Cursor object");

        } else if (cursor.moveToFirst()) {

            /*
             * Validating input for security purposes
             */
            if (!(cursor.getString(cursor.getColumnIndex(UserRegistrationInfo.USER_NAME))
                    .equals(username))) {

                Toast.makeText(this, "Please enter the correct username", Toast.LENGTH_SHORT).show();
            }
            /*
             * Invalid security question
             */
            else if (!(cursor.getString(cursor.getColumnIndex(UserRegistrationInfo.SECURITY_QUESTION))
                    .equals(securityQuestion))) {

                Toast.makeText(this, "Please select the security question you specified during Registration",
                        Toast.LENGTH_SHORT).show();
            }
            /*
             * Invalid security response
             */
            else if (!(cursor.getString(cursor.getColumnIndex(UserRegistrationInfo.SECURITY_RESPONSE))
                    .equals(securityResponse))) {

                Toast.makeText(this, "Your security response is invalid", Toast.LENGTH_SHORT).show();
            }
            /*
             * The username, security question & security response used during
             * the Registration process will be used to validate PIN resetting.
             * User can only change a PIN if a matching record exists from the
             * Register table in the db.
             */
            else if ((cursor.getString(cursor.getColumnIndex(UserRegistrationInfo.USER_NAME))
                    .equals(username))
                    && (cursor.getString(cursor.getColumnIndex(UserRegistrationInfo.SECURITY_QUESTION))
                    .equals(securityQuestion))
                    && (cursor.getString(cursor.getColumnIndex(UserRegistrationInfo.SECURITY_RESPONSE))
                    .equals(securityResponse))) {

                /*
                 * Retrieve the user id to update their details
                 */
                idToUpdate = cursor.getString(cursor.getColumnIndex(UserRegistrationInfo.REG_ID));
                String updateWhereClause = UserRegistrationInfo.REG_ID + " = ?";
                String[] updateWhereArgs = {idToUpdate};

                /*
                 * Define an object to contain the new values to insert
                 */
                ContentValues userRegValues = new ContentValues();
                /*
                 * Get the new values to be updated
                 */
                userRegValues.put(UserRegistrationInfo.EMAIL_ADD, email);
                userRegValues.put(UserRegistrationInfo.USER_PIN, matchedPIN); //new matched pin
                userRegValues.put(UserRegistrationInfo.SECURITY_QUESTION, securityQuestion); //new security question
                userRegValues.put(UserRegistrationInfo.SECURITY_RESPONSE, securityResponse); //new security response

                try {

                    eyeRSContentResolver.update(DBOperations.CONTENT_URI_USER_REG, userRegValues,
                            updateWhereClause, updateWhereArgs);

                    Toast.makeText(this, "Your details have been updated successfully ",
                            Toast.LENGTH_SHORT).show();
                    Log.e("DATABASE OPERATIONS", "...Credentials updated successfully!");

                    /*
                     * Then clear the fields
                     */
                    this.txtUsername.setText("");
                    this.txtEmail.setText("");
                    this.txtPIN1.setText("");
                    this.txtPIN2.setText("");
                    this.txtResponse.setText("");
                    /*
                     * Once credentials are successfully updated,
                     * navigate user back to the Login screen
                     */
                    super.startActivity(new Intent(this, LoginActivity.class));

                } catch (Exception ex) {

                    Log.e("PIN update query", ex.getMessage(), ex);
                    Toast.makeText(this, "Unable to add item", Toast.LENGTH_SHORT).show();
                }

            } else {

                Toast.makeText(this, "PIN reset failed. Please ensure you have entered the correct details",
                        Toast.LENGTH_SHORT).show();
                Log.e("SetPINActivity", "Null Cursor object");

            }

            cursor.close();
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
     * @param emailAddress
     * @return
     */

    public boolean validateEmailAddress(String emailAddress) {

        return regexPattern.matcher(emailAddress).matches();
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

        try {

            /*
             * Save the spinner's selection
             */
            spinner = (Spinner) findViewById(R.id.setPin_spinner);
            SharedPreferences category_prefs = getSharedPreferences("category_prefs", Context.MODE_PRIVATE);
            category_prefs.edit().putInt("spinner_indx", spinner.getSelectedItemPosition()).apply();
        } catch (Exception ex) {

            Log.e("Spinner onPause", ex.getMessage(), ex);
        }

    }

    /**
     * Method allows us to retrieve previous selection before the activity was paused
     */
    @Override
    protected void onResume() {
        super.onResume();

        try {

            /*
             * Retrieve the saved spinner selection
             */
            spinner = (Spinner) findViewById(R.id.setPin_spinner);
            SharedPreferences category_prefs = getSharedPreferences("category_prefs", Context.MODE_PRIVATE);
            int spinner_index = category_prefs.getInt("spinner_indx", 0);
            spinner.setSelection(spinner_index);

        } catch (Exception ex) {

            Log.e("Spinner onResume", ex.getMessage(), ex);
        }

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
