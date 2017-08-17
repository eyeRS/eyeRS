package com.github.eyers.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.github.eyers.R;
import com.github.eyers.StockInfo;
import com.github.eyers.StockLabel;
import com.github.eyers.StockLabelAdapter;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ArrayList<StockLabel> arrayOfUsers = new ArrayList<StockLabel>();
        StockLabelAdapter adapter = new StockLabelAdapter(this, arrayOfUsers);
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);

//        EyeRSDatabaseHelper f = new EyeRSDatabaseHelper();
//    f.getReadableDatabase().beginTransaction();
//        f.getReadableDatabase().qu
//        f.getReadableDatabase().close();
    }
}

