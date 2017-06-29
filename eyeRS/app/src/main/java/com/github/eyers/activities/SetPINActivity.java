package com.github.eyers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Button;
import com.github.eyers.R;

/**
 *
 */
public class SetPINActivity extends AppCompatActivity implements View.OnClickListener {

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
    private EditText txtPIN1;
    private EditText txtPIN2;
    private EditText txtResponse; //retrieves the user's security response
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_set_pin);

        this.txtPIN1 = (EditText) findViewById(R.id.txtPIN1);
        this.txtPIN2 = (EditText) findViewById(R.id.txtPIN2);
        this.txtResponse = (EditText) findViewById(R.id.txtSecurityResponse);

        this.spinner = (Spinner) findViewById(R.id.spinner); //Links to the spinner in the layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, QUESTIONS);
        this.spinner.setAdapter(adapter);

        findViewById(R.id.btnSubmit).setOnClickListener(this);
        findViewById(R.id.btnClear).setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnClear:
                this.txtPIN1.setText("");
                this.txtPIN2.setText("");
                this.txtResponse.setText("");
                return;
            case R.id.btnSubmit:
                String pinA = txtPIN1.getText().toString();
                String pinB = txtPIN1.getText().toString();
                String answer = txtResponse.getText().toString();

                if (pinA == null || pinA.equals("")) {
                    Toast.makeText(this, "Please enter a PIN.", Toast.LENGTH_LONG).show();
                    return;
                } else if (pinA == null || pinA.equals("")) {
                    Toast.makeText(this, "Please confirm you PIN.", Toast.LENGTH_LONG).show();
                    return;
                } else if (!pinA.equals(pinB)) {
                    Toast.makeText(this, "Your PINs do not match.", Toast.LENGTH_LONG).show();
                    return;
                } else if (answer == null || answer.equals("")) {
                    Toast.makeText(this, "Please enter a security question.", Toast.LENGTH_LONG).show();
                    return;
                }

                super.startActivity(new Intent(this, MainActivity.class));
                return;
        }
    }
}
