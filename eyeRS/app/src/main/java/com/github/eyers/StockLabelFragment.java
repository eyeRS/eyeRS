package com.github.eyers;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Nathan Shava on 29-Aug-17.
 */

public class StockLabelFragment extends Fragment {

    //Required empty public constructor
    public StockLabelFragment() {
    }

    private String name, symbol, sector;
    private ImageView itemView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            name = savedInstanceState.getString("name"); //Retrieve the name of the item from the Bundle
            symbol = savedInstanceState.getString("symbol"); //Retrieve the item's symbol from the Bundle
            sector = savedInstanceState.getString("sector"); //Retrieve the item's sector from the Bundle

        }
    }

    //We set the fragment's layout in the onCreateView() method
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View layout = inflater.inflate(R.layout.item_stock_label, container, false);
        return layout;

    }

    //Save the state of the stopwatch if it's about to be destroyed
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("name", name);
        savedInstanceState.putString("symbol", symbol);
        savedInstanceState.putString("sector", sector);

    }

    public String getName() {
        return this.name;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public String getSector() {
        return this.sector;
    }
}
