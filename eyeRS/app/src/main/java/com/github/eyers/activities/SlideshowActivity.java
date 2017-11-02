package com.github.eyers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.eyers.EyeRS;
import com.github.eyers.ItemLabel;
import com.github.eyers.R;
import com.github.eyers.activities.settings.SettingUtilities;
import com.github.eyers.wrapper.ItemWrapper;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class will how items will be viewed in an image-based slideshow setup.
 */
public class SlideshowActivity extends AppCompatActivity {

    // Fields & other declarations
    private ImageView img;
    private ArrayList<ItemWrapper> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SettingUtilities.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_slideshow);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.img = (ImageView) findViewById(R.id.img);

        this.items = new ArrayList<>();
        for (ItemLabel category : EyeRS.getCategoriesList(this)) {
            this.items.addAll(EyeRS.getItems(category.getName(), this));
        }

        final Handler timer = new Handler(); // final for thread
        timer.postDelayed(new Runnable() {

            @Override
            public void run() {
                SlideshowActivity.this.setImage();
                timer.postDelayed(this, 3 * 1000);
            }
        }, 3 * 1000);
        this.setImage();
    }

    private void setImage() {
        if (!items.isEmpty()) {
            ItemWrapper item = items.get(new Random().nextInt(items.size()));
            Toast.makeText(this, item.getName(), Toast.LENGTH_SHORT).show();
            try {
                this.img.setImageBitmap(item.getImage());
            } catch (NullPointerException npe) {
                Log.e("Slideshow image error", "error showing image", npe);
            }
//            Toast.makeText(this, item.getName() + item.getImage(), Toast.LENGTH_SHORT).show();
        }
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
    }

} //end class SlideshowActivity
