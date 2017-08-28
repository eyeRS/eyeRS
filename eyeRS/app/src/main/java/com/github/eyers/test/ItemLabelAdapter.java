package com.github.eyers.test;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.github.eyers.R;

import java.util.ArrayList;

/**
 * Created by Matthew Van der Bijl on 2017/08/16.
 */
public class ItemLabelAdapter extends ArrayAdapter<ItemLabel> {

    public ItemLabelAdapter(Context context, ArrayList<ItemLabel> stocks) {
        super(context, R.layout.item_label, stocks);
    }
}
