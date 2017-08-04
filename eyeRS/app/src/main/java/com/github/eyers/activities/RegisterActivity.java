package com.github.eyers.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
    private Spinner spinner;    //contains the list of security questions

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.txtUsername = (EditText) findViewById(R.id.edtTxtUsername);
        this.txtEmail = (EditText) findViewById(R.id.edtTxtEmail);
        this.txtPIN1 = (EditText) findViewById(R.id.edtTxtCreatePIN);
        this.txtPIN2 = (EditText) findViewById(R.id.edtTxtVerifyPIN);
        this.txtResponse = (EditText) findViewById(R.id.edtTxtSecurityResponses);

        this.spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, QUESTIONS); //Populates the spinner with the array contents

        this.spinner.setAdapter(adapter);
        this.spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener(){

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        position = spinner.getSelectedItemPosition();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );

        findViewById(R.id.btnRegister).setOnClickListener(this);

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnRegister:

                return;

        }
    }
}
