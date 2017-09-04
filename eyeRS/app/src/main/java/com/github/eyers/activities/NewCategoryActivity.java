package com.github.eyers.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.eyers.R;

public class NewCategoryActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText txtTitle;
    private EditText txtDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.txtTitle = (EditText)findViewById(R.id.edtTxtCatTitle);
        this.txtDesc = (EditText)findViewById(R.id.edtTxtCatDesc);

        findViewById(R.id.btnAddCategory).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnAddCategory: //user clicks add

                if (txtTitle != null && txtDesc != null){
                    //User cannot add a new category without a title & description


                }
                else{
                    Toast.makeText(this, "Please add a Title and a Description to successfully" +
                            "create a new category", Toast.LENGTH_LONG).show();
                }

                return;

        }
    }
}
