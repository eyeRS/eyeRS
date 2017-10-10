package com.github.eyers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.eyers.EyeRS;
import com.github.eyers.R;

/**
 * This class will handle the Login event of the app.
 */
public final class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Field declarations
     */
    private EditText txtPIN;

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
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

        //super.startActivity(new Intent(this, CamTestActivity.class));

        switch (v.getId()) {
            case R.id.txtForgotPin:
                super.startActivity(new Intent(this, SetPINActivity.class));
                return;
            case R.id.btnRegister:
                super.startActivity(new Intent(this, RegisterActivity.class));
                return;
            case R.id.btnLogin:
                final String pin = txtPIN.getText().toString();

                if (pin == null || pin.equals("")) {
                    Toast.makeText(this, "Please enter your PIN code.", Toast.LENGTH_SHORT).show();
                    return;
                }

                final String hash = EyeRS.sha256(pin);
                if (hash.equals(EyeRS.sha256("1234"))) { // Matthew: check against the hashed password stored
                    super.startActivity(new Intent(this, MainActivity.class));
                    return;
                } else {
                    Toast.makeText(this, "Incorrect PIN code. Please try again.", Toast.LENGTH_SHORT).show();
                    return;
                }
        }
    }
}
