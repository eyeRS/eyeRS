package com.github.eyers.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.VideoView;

import com.github.eyers.R;
import com.github.eyers.activities.settings.SettingUtilities;

/**
 * This class allows the user to view a video tutorial on how to use the app as well as have the ability
 * to provide feedback to the developers.
 */
public final class HelpActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SettingUtilities.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_help);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.btnFeedback).setOnClickListener(this);

        try {
            final VideoView videoView = (VideoView) findViewById(R.id.videoView);
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tab);
            videoView.setVideoURI(uri);
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                    videoView.start();
                }
            });

        } catch (Exception ex) {
            Log.e("Help video", ex.getMessage(), ex);
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

        try {

            switch (v.getId()) {
                case R.id.btnFeedback:
                    //Toast.makeText(this, "Take the user to the Google Play Store page to leave a comment", Toast.LENGTH_LONG).show();
                    Intent viewIntent = new Intent("android.intent.action.VIEW",
                            Uri.parse("http://www.google.com/"));
                    startActivity(viewIntent);
                    break;
            }

        } catch (Exception ex) {
            Log.e("Help Feedback button", ex.getMessage(), ex);
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

} //end class HelpActivity
