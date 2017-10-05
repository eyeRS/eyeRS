package com.github.eyers.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.github.eyers.R;

/**
 * Created by Nathan Shava on 29-Aug-17.
 */
public class ItemDetail extends Activity {

    /**
     * Field declarations
     */
    public static final String EXTRA_ITEM_ID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        ItemDetailFragment itemDetailFragment = (ItemDetailFragment) //get a reference to the fragment
                getFragmentManager().findFragmentById(R.id.detail_frag);
        //get the ID of the item the user clicked on from the intent
        int itemID = (int) getIntent().getExtras().get(EXTRA_ITEM_ID);
        itemDetailFragment.setItem(itemID);

    }
}
