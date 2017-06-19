package com.github.eyers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.eyers.R;

public final class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText txtPIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_login);

        this.txtPIN = (EditText) findViewById(R.id.txtPIN);

        ((Button) findViewById(R.id.btnLogin)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnForgot)).setOnClickListener(this);

        Toast.makeText(this, "Welcome!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnForgot:
                super.startActivity(new Intent(this, SetPINActivity.class));
                return;
            case R.id.btnLogin:
                String pin = txtPIN.getText().toString();

                if (pin == null || pin.equals("")) {
                    Toast.makeText(this, "Please enter your PIN code.", Toast.LENGTH_SHORT).show();
                    return;
                }

                super.startActivity(new Intent(this, MainActivity.class));
                return;
        }
    }
}
