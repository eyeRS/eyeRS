package com.github.eyers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.github.eyers.R;
import com.github.eyers.wrapper.ItemWrapper;
import com.vj.widgets.AutoResizeTextView;

public class ViewItemActivity extends AppCompatActivity {

    public static ItemWrapper ITEM = null;

    private ImageView image;
    private AutoResizeTextView title;
    private AutoResizeTextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_view_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.title = (AutoResizeTextView) findViewById(R.id.txtTitle);
        this.description = (AutoResizeTextView) findViewById(R.id.txtDescription);
        this.image = (ImageView) findViewById(R.id.img);

        this.title.setText(ITEM.getName());
        this.description.setText(ITEM.getDescription());
        this.image.setImageBitmap(ITEM.getImage());
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
}
