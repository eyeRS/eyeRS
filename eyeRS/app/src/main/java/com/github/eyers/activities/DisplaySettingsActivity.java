package com.github.eyers.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.github.eyers.R;

/**
 * This class will handle display settings events based on the user's preference
 */
public class DisplaySettingsActivity extends AppCompatActivity implements OnClickListener {

    private Spinner fontTypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_settings);

        String[] fontTypes = getResources().getStringArray(R.array.font_types);

        findViewById(R.id.imgbtn_Blue).setOnClickListener(this);
        findViewById(R.id.imgbtn_Red).setOnClickListener(this);
        findViewById(R.id.imgbtn_Yellow).setOnClickListener(this);
        findViewById(R.id.imgbtn_Green).setOnClickListener(this);

        fontTypeSpinner = (Spinner) findViewById(R.id.spinnerFontType);

        /**
         * Event handler for font type spinner
         */
        fontTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void onClick(View view) {

        try {

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

        } catch (Exception ex) {

            Log.e("Change Theme", ex.getMessage(), ex);
        }
    }
} //end class DisplaySettingsActivity




