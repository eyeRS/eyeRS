package com.github.eyers.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.github.eyers.DBOperations;
import com.github.eyers.R;
import com.github.eyers.info.NewRegInfo;

import static com.github.eyers.R.id.touchSwitch;
import static com.github.eyers.R.id.txtPIN;

/**
 * This class will handle sound settings events based on the user's selection. Created by Nathan Shava
 * on 19-Sep-17.
 *
 * @author Nathan Shava
 */
public class SoundSettings extends AppCompatActivity {


  //  @Override

    protected void onCreate(Bundle savedInstanceState) {

      super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Switch welcomeSwitch = (Switch) findViewById(R.id.welcomeSwitch);
        final Switch touchSwitch = (Switch) findViewById(R.id.touchSwitch);

      welcomeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

               /* If the user wishes to receive a welcome message pop up*/
                if (isChecked) {

                    Toast.makeText(SoundSettings.this, "Welcome message enabled!", Toast.LENGTH_SHORT).show();


                }
                /** If the user does not wish to receive a welcome message pop up**/
                if (!isChecked) {

                 Toast.makeText(SoundSettings.this, "Welcome message disabled!", Toast.LENGTH_SHORT).show();

                }

            }
        });

        touchSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                /* If the user wishes to get touch sounds*/
                if (isChecked) {

                    Toast.makeText(SoundSettings.this, "Touch sounds enabled!", Toast.LENGTH_SHORT).show();
                }
                /*If the user does not wish to get touch sounds*/
                if (!isChecked) {

                    Toast.makeText(SoundSettings.this, "Touch sounds disabled!", Toast.LENGTH_SHORT).show();
                }
            }
        });}
    public void checkT(View view){
        boolean check=((Switch) view).isChecked();
    }


} //end class SoundSettings