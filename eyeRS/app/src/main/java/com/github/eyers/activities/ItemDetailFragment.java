package com.github.eyers.activities;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.eyers.ItemLabelFragment;
import com.github.eyers.R;


/**
 * Class will be used to display details of items based on users' selection.
 * Created by Nathan Shava on 29-Aug-17.
 *
 * @author Nathan Shava
 */
public class ItemDetailFragment extends Fragment {

    /**
     * The ID of the item the user will select.
     */
    private long itemID;

    /**
     * Required empty public constructor.
     */
    public ItemDetailFragment() {
        super();
    }

    /**
     * FragmentTransactions only work on this target API or better.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            itemID = savedInstanceState.getLong("workoutID");
        } else {
            //Use a fragment transaction to add the Stock Label fragment to the frame layout
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            ItemLabelFragment itemLabelFragment = new ItemLabelFragment();
            ft.replace(R.id.fragment_container, itemLabelFragment);
            ft.addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();

        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.item_label, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        //Gets the fragment's root View. This can be used to get references to the item name and symbol and sector views
        View view = getView();

        if (view != null) {
            TextView name = (TextView) view.findViewById(R.id.lblName);
            ItemLabelFragment itemLabelFragment = new ItemLabelFragment();
//            name.setText(itemLabelFragment.getName());
//            TextView symbol = (TextView) view.findViewById(R.id.lblSymbol);
//            symbol.setText(itemLabelFragment.getSymbol());
//            TextView sector = (TextView) view.findViewById(R.id.lblShift);
//            sector.setText(itemLabelFragment.getSector());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putLong("itemID", itemID);
    }

    /**
     * The activity will use this Setter to set the value of the itemID.
     *
     * @param id
     */
    public void setItem(long id) {
        this.itemID = id;
    }
}
