package com.github.eyers.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.github.eyers.R;

public class NewItemActivity extends Activity implements View.OnClickListener {

    private static final String[] CATEGORIES = {
        "Books", "Clothes", "Games", "Accessories", "Other"
    };

    private ImageButton photo;
    private EditText txtTitle;
    private EditText txtDescription;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        this.photo = (ImageButton) findViewById(R.id.new_item_image);
        this.txtTitle =  (EditText) findViewById(R.id.edtTxtTitle);
        this.txtDescription = (EditText) findViewById(R.id.edtTxtDescription);
        this.spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, CATEGORIES); //Populates the spinner with the array contents

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

        findViewById(R.id.btnAdd);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnAdd:

                return;

        }
    }


}
