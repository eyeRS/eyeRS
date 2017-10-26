package com.github.eyers.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.eyers.DBOperations;
import com.github.eyers.R;
import com.github.eyers.info.UserRegistrationInfo;

/**
 * This class will handle the Login event of the app.
 *
 * @see View.OnClickListener
 * @see AppCompatActivity
 */
public final class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    MediaPlayer welcomeMessage;
    private EditText txtPIN;
    private Button registerButton;
    private Button loginButton;
    /**
     * Content Resolver declaration.
     */
    private ContentResolver eyeRSContentResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        super.setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.txtPIN = (EditText) findViewById(R.id.txtPIN);

        this.loginButton = (Button) findViewById(R.id.btnLogin);
        this.loginButton.setOnClickListener(this);
        findViewById(R.id.txtForgotPin).setOnClickListener(this);
        this.registerButton = (Button) findViewById(R.id.btnRegister);
        this.registerButton.setOnClickListener(this);

        /**
         * Content resolver object
         */
        eyeRSContentResolver = this.getContentResolver();

        String[] projection = {
                UserRegistrationInfo.REG_ID,
                UserRegistrationInfo.USER_NAME,
                UserRegistrationInfo.EMAIL_ADD,
                UserRegistrationInfo.USER_PIN,
                UserRegistrationInfo.SECURITY_QUESTION,
                UserRegistrationInfo.SECURITY_RESPONSE
        };

        String whereClause = "";
        String[] whereArgs = {};
        String sortOrder = "";

        try {
            /**
             * Content Resolver query
             */
            Cursor cursor = eyeRSContentResolver.query(DBOperations.CONTENT_URI_USER_REG, projection,
                    whereClause, whereArgs, sortOrder);

            if (!cursor.moveToFirst()) {

                /**
                 * No user registered so disable the Login button
                 */
                this.loginButton.setEnabled(false);
                Log.e("Null Cursor object", "Unable to retrieve cursor data");

            } else if (cursor.moveToFirst()) {

                /**
                 * If a user has been registered already
                 * we need to disable the Register button to follow
                 * the One user per device policy
                 */
                this.registerButton.setEnabled(false);

                cursor.close();
            }
        } catch (Exception ex) {

            Log.e("Login query", ex.getMessage(), ex);
        }

        /*Initialising mediaPlayer*/
        welcomeMessage = MediaPlayer.create(LoginActivity.this, R.raw.welcomemsg);
    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.txtForgotPin:
                    super.startActivity(new Intent(this, SetPINActivity.class));
                    break;
                case R.id.btnRegister:
                    super.startActivity(new Intent(this, RegisterActivity.class));
                    break;
                case R.id.btnLogin:
                    verifyLoginPIN(); //method to validate Login process
                    break;
            }
        } catch (Exception ex) {
            Log.e("Login event handlers", ex.getMessage());
        }
    }

    public void verifyLoginPIN() {

        eyeRSContentResolver = getApplicationContext().getContentResolver(); //Content resolver object

        String[] projection = {
                UserRegistrationInfo.REG_ID,
                UserRegistrationInfo.USER_NAME,
                UserRegistrationInfo.EMAIL_ADD,
                UserRegistrationInfo.USER_PIN,
                UserRegistrationInfo.SECURITY_QUESTION,
                UserRegistrationInfo.SECURITY_RESPONSE
        };

        String whereClause = "";

        String[] whereArgs = {};

        String sortOrder = "";

        try {
            /**
             * Content Resolver query
             */
            Cursor cursor = eyeRSContentResolver.query(DBOperations.CONTENT_URI_USER_REG, projection,
                    whereClause, whereArgs, sortOrder);

            if (!cursor.moveToFirst()) {
                Toast.makeText(this, "Login failed. Please ensure you have registered your details first before " +
                        "attempting to login", Toast.LENGTH_SHORT).show();
            } else if (cursor.moveToFirst()) {

                /**
                 * Enable the Login button
                 */
                this.loginButton.setEnabled(true);
                boolean flag = true;
                do {
                    if (cursor.getString(cursor.getColumnIndex(UserRegistrationInfo.USER_PIN)).equals("")) { //No PIN entered
                        Toast.makeText(this, "Login failed. Please insert a valid PIN to login successfully", Toast.LENGTH_SHORT).show();
                    }
                    if (cursor.getString(cursor.getColumnIndex(UserRegistrationInfo.USER_PIN)).equals(txtPIN.getText().toString())) { //Correct PIN entered
                        super.startActivity(new Intent(getApplicationContext(), MainActivity.class)); //Grant access
                        welcomeMessage.start(); // Welcome message
                        flag = false;
                    }
                } while (cursor.moveToNext());

                cursor.close();

                if (flag) {

                    Toast.makeText(this, "Login failed. Please enter the correct PIN", Toast.LENGTH_SHORT).show();
                }
            } else {

                Toast.makeText(this, "Login failed. Please ensure you have registered your details first before " +
                        "attempting to login", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {

            Log.e("Login query", ex.getMessage(), ex);
        }

    }
} //end class Login
