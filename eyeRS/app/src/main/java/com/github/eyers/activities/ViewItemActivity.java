package com.github.eyers.activities;

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
        setContentView(R.layout.activity_view_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.title = (AutoResizeTextView) findViewById(R.id.txtTitle);
        this.description = (AutoResizeTextView) findViewById(R.id.txtDescription);
        this.image = (ImageView) findViewById(R.id.img);

        this.title.setText(ITEM.getName());
        this.description.setText(ITEM.getDescription());
        this.image.setImageBitmap(ITEM.getImage());
//        this.image.setScaleType(ImageView.ScaleType.FIT_XY);

//        getIntent().setAction("Already created");
    }

//    @Override
//    protected void onResume() {
//        Log.v("Example", "onResume");
//
//        String action = getIntent().getAction();
    // Prevent endless loop by adding a unique action, don't restart if action is present
//        if(action == null || !action.equals("Already created")) {
//            Log.v("Example", "Force restart");
//            Intent intent = new Intent(this, ViewItemActivity.class);
//            startActivity(intent);
//            finish();
//        }
//        // Remove the unique action so the next time onResume is called it will restart
//        else
//            getIntent().setAction(null);
//
//        super.onResume();
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ECLAIR
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            // Take care of calling this method on earlier versions of
            // the platform where it doesn't exist.
            onBackPressed();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        MainActivity.STATE = "main";
        super.onBackPressed();
    }
}
