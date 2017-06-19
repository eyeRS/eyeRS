package com.github.eyers;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

public class Login extends Activity {

    private String pin; //Holds the user's PIN input
    private Button continueBtn; //Used to validate login details

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void forgotPinAction(){ //Executes when the user has forgotten their pin

    }

    public void continueAction(){ //Executes when the Continue button is clicked

    }
}
