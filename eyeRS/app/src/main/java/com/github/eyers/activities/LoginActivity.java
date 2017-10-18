package com.github.eyers.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.eyers.DBOperations;
import com.github.eyers.R;
import com.github.eyers.info.NewRegInfo;

/**
 * This class will handle the Login event of the app.
 */
public final class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Field declarations
     */
    private EditText txtPIN;
    /**
     * Content Resolver declaration.
     */
    private ContentResolver eyeRSContentResolver;
    /*Getting media player*/
        MediaPlayer welcomeMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.txtPIN = (EditText) findViewById(R.id.txtPIN);

        findViewById(R.id.btnLogin).setOnClickListener(this);
        findViewById(R.id.txtForgotPin).setOnClickListener(this);
        findViewById(R.id.btnRegister).setOnClickListener(this);

        Toast.makeText(this, "Welcome!", Toast.LENGTH_SHORT).show();

        /*Initialising mediaPlayer*/
        welcomeMessage=MediaPlayer.create(LoginActivity.this,R.raw.welcomemsg);
    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.txtForgotPin:
                super.startActivity(new Intent(this, SetPINActivity.class));
                return;
            case R.id.btnRegister:
                super.startActivity(new Intent(this, RegisterActivity.class));
                return;
            case R.id.btnLogin:
                verifyLoginPIN(); //method to validate Login PIN
        }
    }

    public void verifyLoginPIN(){

        eyeRSContentResolver = getApplicationContext().getContentResolver(); //Content resolver object

        String[] projection = {
                NewRegInfo.UserRegistrationInfo.REG_ID,
                NewRegInfo.UserRegistrationInfo.USER_NAME,
                NewRegInfo.UserRegistrationInfo.EMAIL_ADD,
                NewRegInfo.UserRegistrationInfo.USER_PIN,
                NewRegInfo.UserRegistrationInfo.SECURITY_QUESTION,
                NewRegInfo.UserRegistrationInfo.SECURITY_RESPONSE};

        Cursor cursor = eyeRSContentResolver.query(DBOperations.CONTENT_URI_USER_REG,
                projection, null, null,
                null);

        if (cursor.moveToFirst()) {

            do {

                /**
                 * We need to retrieve the pin used during the Register Activity
                 * to validate the Login process
                 */
                if (cursor.getString(cursor.getColumnIndex(NewRegInfo.UserRegistrationInfo.USER_PIN)
                ).equals(txtPIN.getText().toString())) {

                    super.startActivity(new Intent(getApplicationContext(), MainActivity.class)); //Grant access
                    /*Starting media*/
                    welcomeMessage.start();
                }
                else{

                    Toast.makeText(this, "Login failed. Please enter the correct PIN", Toast.LENGTH_SHORT).show();
                }

            } while (cursor.moveToNext());

            cursor.close();

        }
    }
}
