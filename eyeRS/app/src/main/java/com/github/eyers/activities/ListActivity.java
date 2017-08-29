package com.github.eyers.activities;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.github.eyers.R;
import com.github.eyers.StockLabel;
import com.github.eyers.StockLabelAdapter;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity
        implements ItemListFragment.ItemListListener {

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

    @Override
    public void itemClicked(long id) {

        View fragmentContainer = findViewById(R.id.fragment_container);

        if (fragmentContainer != null) { //Only execute if the frame layout is available

            ItemDetailFragment items = new ItemDetailFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction(); //Start the fragment transaction
            items.setItem(id);
            ft.replace(R.id.fragment_container, items);
            ft.addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE); //Get the new and old fragments to fade in and out
            ft.commit(); //Commit the transaction
        }
        else{

            Intent intent = new Intent(this, ItemDetail.class);
            intent.putExtra(ItemDetail.EXTRA_ITEM_ID, (int)id);
            startActivity(intent);
        }
    }
}

