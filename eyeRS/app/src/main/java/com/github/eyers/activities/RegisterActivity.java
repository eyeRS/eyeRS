package com.github.eyers.activities;

import android.content.ContentValues;
import android.content.Intent;
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
 * @see android.view.View.OnClickListener
 * @see android.support.v7.app.AppCompatActivity
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener,
    OnItemSelectedListener {

    /**
     * The possible security questions.
     */
    private static final String[] QUESTIONS = {

            "What is the name of your junior/primary school?",
            "What is the name of your first pet?",
            "In what year was your father born?",
            "In what city does your nearest sibling stay?",
            "What is the first name of the teacher who gave you your first failing grade?",
            "What was the house number or street name you lived in as a child?",
            "What were the last 4 digits of your childhood mobile number?",
            "In what city or town was your first full time job?",
            "What are the last 5 digits of your ID number?",
            "What time of the day were you born (hh:mm)?"
    };

    //Declarations
    private static String username;
    private static String email;
    private static String matchedPIN;
    private static String securityQuestion;
    private static String securityResponse;

    //db variables
    public SQLiteDatabase db;
    private EyeRSDatabaseHelper eyeRSDatabaseHelper;

    //Fields
    private EditText txtUsername;
    private EditText txtEmail;
    private EditText txtPIN1;
    private EditText txtPIN2;
    private EditText txtResponse; //retrieves the user's security response
    private Spinner spinner;    //contains the list of security questions

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


        this.spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, QUESTIONS); //Populates the spinner with the array contents

        this.spinner.setAdapter(adapter);
        this.spinner.setOnItemSelectedListener(this);

        findViewById(R.id.btnRegister).setOnClickListener(this);

    }

    //Open the database connection
    public RegisterActivity open() {
        db = eyeRSDatabaseHelper.getWritableDatabase();
        return this;
    }

    //Close the connection
    public void close() {
        eyeRSDatabaseHelper.close();
    }

    //Method to add user's Registration details
    public void addRegInfo() {

        open(); //open the db connection

        ContentValues userRegValues = new ContentValues();
        //Insert the user's name
        userRegValues.put(NewRegInfo.UserRegistrationInfo.USER_NAME, username);
        //Insert the user's email address
        userRegValues.put(NewRegInfo.UserRegistrationInfo.EMAIL_ADD, email);
        //Insert the user's pin
        userRegValues.put(NewRegInfo.UserRegistrationInfo.USER_PIN, matchedPIN);
        //Insert the user's security response
        userRegValues.put(NewRegInfo.UserRegistrationInfo.SECURITY_RESPONSE, securityResponse);

        try {

            db.beginTransaction();

            //Insert the user registration details into the db
            db.insert(NewRegInfo.UserRegistrationInfo.TABLE_NAME, null,
                    userRegValues);

            Toast.makeText(this, "Your details have been saved successfully ", Toast.LENGTH_LONG).show();

            //Display message in the logcat window after successful operation execution
            Log.e("DATABASE OPERATIONS", "...New user added to DB!");
        } catch (SQLException ex) {
            Toast.makeText(this, "Unable to add item", Toast.LENGTH_SHORT).show();
        } finally {
            db.endTransaction();
        }

        close(); //close the db connection
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
            case R.id.btnRegister:

                username = txtUsername.getText().toString();
                email = txtEmail.getText().toString();
                securityResponse = txtResponse.getText().toString();

                if (pinA.equals(pinB)) { //if the PINs match then get a copy for the db
                    matchedPIN = txtPIN2.getText().toString();

                    open(); //open db

                    addRegInfo(); //call the method to add details to the db

                    close(); //close db

                    //Navigate to the Login screen once registration has been successful
                    super.startActivity(new Intent(this, LoginActivity.class));
                }
                return;
            case R.id.btnClearReg: //user clicks on the Clear button
                this.txtUsername.setText("");
                this.txtEmail.setText("");
                this.txtPIN1.setText("");
                this.txtPIN2.setText("");
                this.txtResponse.setText("");

        }
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

        //Question selected from Spinner
        securityQuestion = parent.getItemAtPosition(position).toString();


    }

    /**
     *
     * @param parent
     * Method handles what happens when nothing is selected from the spinner
     *
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
