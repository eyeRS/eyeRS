package com.github.eyers.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.github.eyers.R;

/**
 * This class will handle sound settings events based on the user's selection. Created by Nathan Shava
 * on 19-Sep-17.
 *
 * @author Nathan Shava
 */
public class SoundSettings extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_sound_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Switch welcomeSwitch = (Switch) findViewById(R.id.welcomeSwitch);

        welcomeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

               /** If the user wishes to receive a welcome message pop up*/
                if (isChecked) {

                    Toast.makeText(SoundSettings.this, "Welcome message enabled!", Toast.LENGTH_SHORT).show();


                }
                /** If the user does not wish to receive a welcome message pop up**/
                if (!isChecked) {

                    Toast.makeText(SoundSettings.this, "Welcome message disabled!", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }
} //end class SoundSettings