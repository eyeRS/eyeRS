package com.github.eyers.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.github.eyers.R;

public class ItemSortingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_sorting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Switch asc_descSwitch = (Switch) findViewById(R.id.item_asc_descSwitch);
        Switch recently_addSwitch = (Switch) findViewById(R.id.item_recently_add_Switch);

        asc_descSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                /** If the user wishes to sort alphabetically */
                if (isChecked) {

                    Toast.makeText(ItemSortingActivity.this, "All items have been sorted alphabetically",
                            Toast.LENGTH_SHORT).show();
                }
                /** If the user does not wish to sort alphabetically */
                if (!isChecked) {

                    Toast.makeText(ItemSortingActivity.this, "All items will be displayed as normal",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        recently_addSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                /** If the user wishes to sort by the most recently added items */
                if (isChecked) {

                    Toast.makeText(ItemSortingActivity.this, "All items have been sorted according" +
                            " to their order of entry", Toast.LENGTH_SHORT).show();
                }
                /** If the user does not wish to sort alphabetically */
                if (!isChecked) {

                    Toast.makeText(ItemSortingActivity.this, "All items will be displayed as normal",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
