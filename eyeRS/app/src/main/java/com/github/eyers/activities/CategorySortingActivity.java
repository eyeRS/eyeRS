package com.github.eyers.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.github.eyers.R;

/**
 * This class will handle category sorting based on the user's preference.
 *
 * @see AppCompatActivity
 */
public class CategorySortingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_category_sorting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Switch asc_descSwitch = (Switch) findViewById(R.id.cat_asc_descSwitch);
        final Switch recently_addSwitch = (Switch) findViewById(R.id.cat_recently_add_Switch);

        asc_descSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                /* If the user wishes to sort alphabetically */
                if (isChecked) {

                    try {

                        recently_addSwitch.setEnabled(false);
                        Toast.makeText(CategorySortingActivity.this, "All categories have been sorted alphabetically",
                                Toast.LENGTH_SHORT).show();

                    } catch (Exception ex) {

                        Log.e("CategorySort switch", ex.getMessage(), ex);
                    }
                }
                /* If the user does not wish to sort alphabetically */
                if (!isChecked) {

                    try {

                        recently_addSwitch.setEnabled(true);
                        Toast.makeText(CategorySortingActivity.this, "All categories will be displayed as normal",
                                Toast.LENGTH_SHORT).show();

                    } catch (Exception ex) {

                        Log.e("CategorySort switch", ex.getMessage(), ex);
                    }
                }
            }
        });

        recently_addSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                /* If the user wishes to sort by the most recently added items */
                if (isChecked) {

                    try {

                        asc_descSwitch.setEnabled(false);
                        Toast.makeText(CategorySortingActivity.this, "All categories have been sorted according" +
                                " to their order of entry", Toast.LENGTH_SHORT).show();

                    } catch (Exception ex) {

                        Log.e("Recently added switch", ex.getMessage(), ex);
                    }
                }

                /* If the user does not wish to sort alphabetically */
                if (!isChecked) {

                    try {

                        asc_descSwitch.setEnabled(true);
                        Toast.makeText(CategorySortingActivity.this, "All categories will be displayed as normal",
                                Toast.LENGTH_SHORT).show();

                    } catch (Exception ex) {

                        Log.e("Recently added switch", ex.getMessage(), ex);
                    }
                }
            }
        });
    }

} //end class CategorySortingActivity
