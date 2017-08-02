package com.github.eyers.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.github.eyers.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

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

    private EditText txtUsername;
    private EditText txtEmail;
    private EditText txtPIN1;
    private EditText txtPIN2;
    private EditText txtResponse; //retrieves the user's security response
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    public void onClick(View v) {

    }
}
