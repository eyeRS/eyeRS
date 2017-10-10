package com.github.eyers.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.view.View.OnClickListener;
import android.widget.Spinner;

import com.github.eyers.R;

import java.util.List;

import static com.github.eyers.R.layout.content_display_settings;

/**
 * This class will handle display settings events based on the user's preference
 */
public class DisplaySettingsActivity extends AppCompatActivity implements OnClickListener {

    //private Spinner spinner1, spinner2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            Utils.onActivityCreateSetTheme(this);
            setContentView(content_display_settings);

        //adding items to the font type spinner
        Spinner spinnerFontType=(Spinner)findViewById(R.id.spinnerFontType);
        String[] fontType=getResources().getStringArray(R.array.font_types);
        //addinf items to the font size spinner
        Spinner spinnerFontSize=(Spinner)findViewById(R.id.spinnerFontSize);
        String[] fontSize=getResources().getStringArray(R.array.font_size);

        findViewById(R.id.imgbtBlue).setOnClickListener(this);
            findViewById(R.id.imgbtRed).setOnClickListener(this);
            findViewById(R.id.imgbtYellow).setOnClickListener(this);
            findViewById(R.id.imgbtGreen).setOnClickListener(this);






        }

        public void onClick(View view)
        {
            // TODO Auto-generated method stub
            switch (view.getId())
            {

                case R.id.imgbtBlue:
                    Utils.changeToTheme(this, Utils.App_Theme);
                    break;
                case R.id.imgbtRed:
                    Utils.changeToTheme(this, Utils.AppTheme_Red);
                    break;
                case R.id.imgbtYellow:
                    Utils.changeToTheme(this, Utils.AppTheme_Yellow);
                    break;
                case R.id.imgbtGreen:
                    Utils.changeToTheme(this, Utils.AppTheme_Green);
                    break;

            }


        }



    }
/**
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_settings);
/**
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

*/



