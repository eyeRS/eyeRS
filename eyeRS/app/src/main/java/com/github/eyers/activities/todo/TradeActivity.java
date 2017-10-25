package com.github.eyers.activities.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.github.eyers.EyeRS;
import com.github.eyers.ItemLabel;
import com.github.eyers.LabelAdapter;
import com.github.eyers.R;
import com.github.eyers.activities.MainActivity;
import com.github.eyers.wrapper.ItemWrapper;

import java.util.ArrayList;

/**
 * TradeActivity. Created by Matthew Van der Bijl on 20/10/2017.
 *
 * @author Matthew Van der Bijl
 * @since 20/10/2017
 *
 * @see AdapterView.OnItemClickListener
 * @see AppCompatActivity
 */
public class TradeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    /**
     * List view to display all stored items.
     */
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ArrayList<ItemLabel> items = new ArrayList<>();

        for (String category : EyeRS.getCategoriesList(this)) {
            for (ItemWrapper item : EyeRS.getItems(category, this)) {
                items.add(new ItemLabel(item.getName(), item.getImage(), ""));
            }
        }

        LabelAdapter adapter = new LabelAdapter(this, items);

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Toast.makeText(this, "Please select the item you wish to trade", Toast.LENGTH_LONG).show();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ECLAIR
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        MainActivity.STATE = "main";
        super.startActivity(new Intent(this, MainActivity.class));
        super.finish();
        super.onBackPressed();
    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String item = listView.getItemAtPosition(position).toString();

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "TESTING SHARE: " + item);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
}
