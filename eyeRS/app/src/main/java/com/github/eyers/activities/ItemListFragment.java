package com.github.eyers.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.github.eyers.ItemLabel;
import com.github.eyers.LabelAdapter;

import java.util.ArrayList;

/**
 * Created by Nathan Shava on 29-Aug-17.
 *
 * @author Nathan Shava
 */
public class ItemListFragment extends ListFragment {

    private ItemListListener listener;

    /**
     * Required empty public constructor.
     */
    public ItemListFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ArrayList<ItemLabel> arrayOfUsers = new ArrayList<ItemLabel>();

        //Create the LabelAdapter object
        LabelAdapter adapter = new LabelAdapter(inflater.getContext(), arrayOfUsers);

        //Bind the adapter to the list view
        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * Called when the fragment gets attached to the activity.
     *
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.listener = (ItemListListener) activity;
    }

    /**
     * Tell the listener when an item in the ListView is clicked.
     *
     * @param listView
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        if (listener != null) {
            listener.itemClicked(id);
        }
    }

    /**
     * Handles events for items clicked in the list.
     */
    interface ItemListListener {
        void itemClicked(long id);
    }
}
