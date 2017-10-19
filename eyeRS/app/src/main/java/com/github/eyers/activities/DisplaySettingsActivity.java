package com.github.eyers.activities;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.constraint.solver.SolverVariable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.eyers.R;

import org.w3c.dom.Text;



/**
 * This class will handle display settings events based on the user's preference
 */
public class DisplaySettingsActivity extends AppCompatActivity implements OnClickListener {

    private Spinner fontTypeSpinner, fontSizeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_settings);

        String[] fontTypes = getResources().getStringArray(R.array.font_types);
        String[] fontSizes = getResources().getStringArray(R.array.font_size);

        findViewById(R.id.imgbtn_Blue).setOnClickListener(this);
        findViewById(R.id.imgbtn_Red).setOnClickListener(this);
        findViewById(R.id.imgbtn_Yellow).setOnClickListener(this);
        findViewById(R.id.imgbtn_Green).setOnClickListener(this);

        fontTypeSpinner = (Spinner) findViewById(R.id.spinnerFontType);
        fontSizeSpinner = (Spinner) findViewById(R.id.spinnerFontSize);

        /**
         * Event handler for font type spinner
         */
        fontTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Fonts path
                String Default="fonts/ArialNarrowFett.ttf";
                String Arial="fonts/arial.ttf";
                String Antonio = "fonts/Antonio-Bold.ttf";
                String Candy= "fonts/Candy Shop Personal Use.ttf";
                String DroidSans= "fonts/DroidSans-Bold.ttf";

                TextView viewChangeTheme = (TextView) findViewById(R.id.txtViewChangeTheme);
                TextView viewChange_Theme = (TextView) findViewById(R.id.txtView_ChangeTheme);
                TextView ViewChangeFont= (TextView) findViewById(R.id.txtViewChangeFont);
                TextView ViewChange_Font= (TextView) findViewById(R.id.txtViewChange_Font);


                if(position==0){
                    Typeface tf = Typeface.createFromAsset(getAssets(), Default);
                    viewChangeTheme.setTypeface(tf);
                    viewChange_Theme.setTypeface(tf);
                    ViewChangeFont.setTypeface(tf);
                    ViewChange_Font.setTypeface(tf);



                }
                else if (position==1){
                    Typeface tf = Typeface.createFromAsset(getAssets(), Arial);
                    viewChangeTheme.setTypeface(tf);
                    viewChange_Theme.setTypeface(tf);

                }
                else if(position==2){
                    Typeface tf = Typeface.createFromAsset(getAssets(), Antonio);
                    viewChangeTheme.setTypeface(tf);
                    viewChange_Theme.setTypeface(tf);

                }
                else if(position==3){
                    Typeface tf = Typeface.createFromAsset(getAssets(), Candy);
                    viewChangeTheme.setTypeface(tf);
                    viewChange_Theme.setTypeface(tf);

                }
                else if(position==4){
                    Typeface tf = Typeface.createFromAsset(getAssets(), DroidSans);
                    viewChangeTheme.setTypeface(tf);
                    viewChange_Theme.setTypeface(tf);
                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /**
         * Event handler for font size spinner
         */
        fontSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgbtn_Blue:
                Utils.changeToTheme(this, Utils.App_Theme);
                break;
            case R.id.imgbtn_Red:
                Utils.changeToTheme(this, Utils.AppTheme_Red);
                break;
            case R.id.imgbtn_Yellow:
                Utils.changeToTheme(this, Utils.AppTheme_Yellow);
                break;
            case R.id.imgbtn_Green:
                Utils.changeToTheme(this, Utils.AppTheme_Green);
                break;
        }
    }
} //end class DisplaySettingsActivity




