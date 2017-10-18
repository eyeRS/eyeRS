package com.github.eyers.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
        setContentView(R.layout.activity_view_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.title = (AutoResizeTextView) findViewById(R.id.txtTitle);
        this.description = (AutoResizeTextView) findViewById(R.id.txtDescription);
        this.image = (ImageView) findViewById(R.id.img);

        this.title.setText(ITEM.getName());
        this.description.setText(ITEM.getDescription());
        this.image.setImageBitmap(ITEM.getImage());
        this.image.setScaleType(ImageView.ScaleType.FIT_XY);
    }
}
