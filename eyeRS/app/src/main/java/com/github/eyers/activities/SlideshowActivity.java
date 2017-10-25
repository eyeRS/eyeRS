package com.github.eyers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.github.eyers.EyeRS;
import com.github.eyers.ItemLabel;
import com.github.eyers.R;
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
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_slideshow);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.img = (ImageView) findViewById(R.id.img);

        this.items = new ArrayList<>();
        for (ItemLabel category : EyeRS.getCategoriesList(this)) {
            this.items.addAll(EyeRS.getItems(category.getName(), this));
        }

        if (!items.isEmpty()) {
            final Handler timer = new Handler(); // final for thread
            timer.postDelayed(new Runnable() {

                @Override
                public void run() {
                    SlideshowActivity.this.setImage();
                    timer.postDelayed(this, 2 * 1000);
                }
            }, 2 * 1000);
            this.setImage();
        }
    }

    private void setImage() {
        if (!items.isEmpty()) {
            this.img.setImageBitmap(items.get(new Random().nextInt(items.size())).getImage());
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
