package com.github.eyers.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.eyers.R;

public class NewItemActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String[] CATEGORIES = {
            "Books", "Clothes", "Games", "Accessories", "Other"
    };

    private ImageButton photo;
    private EditText txtTitle;
    private EditText txtDesc;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.photo = (ImageButton) findViewById(R.id.new_item_image);
        this.txtTitle = (EditText) findViewById(R.id.edtTxtTitle);
        this.txtDesc = (EditText) findViewById(R.id.edtTxtDescription);
        this.spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, CATEGORIES); //Populates the spinner with the array contents

        this.spinner.setAdapter(adapter);
        this.spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        position = spinner.getSelectedItemPosition();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );

        findViewById(R.id.btnAddItem).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnAddItem: //user clicks add

                if (txtTitle != null ){
                    //User cannot add a new item without a title, description may be null although not advisable
                    if (txtDesc == null){
                        Toast.makeText(this, "You should consider adding a Description for " +
                                "your item", Toast.LENGTH_LONG).show();
                    }


                }
                else{
                    Toast.makeText(this, "Please add a Title to successfully" +
                            "add a new item", Toast.LENGTH_LONG).show();
                }

                return;

        }
    }


}
