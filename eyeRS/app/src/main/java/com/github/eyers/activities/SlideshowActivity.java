package com.github.eyers.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.github.eyers.R;

import java.util.Random;

/**
 *
 */
public class SlideshowActivity extends AppCompatActivity {

    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slideshow);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.img = (ImageView) findViewById(R.id.img);

        final Handler timer = new Handler(); // final for thread
        timer.postDelayed(new Runnable() {

            @Override
            public void run() {
                SlideshowActivity.this.setImage();
                timer.postDelayed(this, 5000);
            }
        }, 5000);
        this.setImage();
    }

    private void setImage() {
        this.img.setImageDrawable(getResources().getDrawable(getImg()));
    }

    private int getImg() {
        int[] img = {
                R.drawable.ic_action_about,
                R.drawable.ic_action_help,
                R.drawable.ic_action_settings,
                R.drawable.ic_info,
                R.drawable.ic_action_voice_search,
                R.drawable.ic_menu_send,
                R.drawable.ic_menu_trade
        };

        return img[new Random().nextInt(img.length - 1)];
    }
}
