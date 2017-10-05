package com.github.eyers;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Nathan Shava on 29-Aug-17.
 */
public class ItemLabelFragment extends Fragment {

    private String name, symbol, sector;
    private ImageView itemView;

    /**
     * Required empty public constructor.
     */
    public ItemLabelFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {

            name = savedInstanceState.getString("name");
            symbol = savedInstanceState.getString("symbol");
            sector = savedInstanceState.getString("sector");

        }
    }

    /**
     * We set the fragment's layout in the onCreateView() method.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.item_label, container, false);
        return layout;
    }

    /**
     * Save the state of the stopwatch if it's about to be destroyed.
     *
     * @param savedInstanceState
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("name", name);
        savedInstanceState.putString("symbol", symbol);
        savedInstanceState.putString("sector", sector);
    }
}
