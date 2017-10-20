package com.github.eyers.activities;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.eyers.DBOperations;
import com.github.eyers.R;
import com.github.eyers.info.NewItemInfo;

/**
 * This class will how items will be viewed in an image-based slideshow setup.
 */
public class SlideshowActivity extends AppCompatActivity {

    /**
     * Field & other declarations
     */
    private Bitmap[] itemImages;
    private ImageView images;
    private String[] imgReferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slideshow);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.images = (ImageView) findViewById(R.id.images);

        final Handler timer = new Handler(); // final for thread
        timer.postDelayed(new Runnable() {

            @Override
            public void run() {
                SlideshowActivity.this.imgReferencesToBitmaps();
                timer.postDelayed(this, 3000);
            }
        }, 3000);
        this.imgReferencesToBitmaps();
    }

    /**
     * Method to retrieve the item images from the db to display in the slideshow
     *
     * @return
     */
    private String[] getImageReferences() {

        /**
         * Content resolver declaration
         */
        ContentResolver eyeRSContentResolver = this.getContentResolver(); //Content resolver object

        String[] projection = {
                NewItemInfo.ItemInfo.ITEM_ID,
                NewItemInfo.ItemInfo.CATEGORY_NAME,
                NewItemInfo.ItemInfo.ITEM_NAME,
                NewItemInfo.ItemInfo.ITEM_DESC,
                NewItemInfo.ItemInfo.ITEM_IMAGE
        };

        String itemsWhereClause = "";

        String[] selectionArgs = {};

        String sortOrder = NewItemInfo.ItemInfo.ITEM_NAME;

        try {

            /**
             * Content resolver query
             */
            Cursor cursor = eyeRSContentResolver.query(
                    DBOperations.CONTENT_URI_ITEMS,
                    projection,
                    itemsWhereClause,
                    selectionArgs,
                    sortOrder);

            if (!cursor.moveToFirst()) {

                Log.e("Slideshow images", "Null Cursor object");
                Toast.makeText(this, "No images found", Toast.LENGTH_SHORT).show();

            } else if (cursor.moveToFirst()) {

                do {

                    for (int i = 0; i < cursor.getCount(); i++) {

                        imgReferences = new String[cursor.getCount()];
                        imgReferences[i] = cursor.getString(
                                Integer.parseInt(NewItemInfo.ItemInfo.ITEM_IMAGE));
                    }

                } while (cursor.moveToNext());

                cursor.close();

            } else {

                Toast.makeText(this, "Nothing to display", Toast.LENGTH_SHORT).show();

            }

        } catch (Exception ex) {

            Log.e("Categories list", ex.getMessage(), ex);
        }

        return imgReferences;

    } //end String[] getImageReferences()

    private void imgReferencesToBitmaps() {

        for (int i = 0; i < imgReferences.length; i++) {

            try {

                /**
                 * Retrieves the String references of the images from the db
                 */
                imgReferences = getImageReferences();
                itemImages = new Bitmap[imgReferences.length];


                /**
                 * String to Bitmap conversion
                 */
                byte[] encodeByte = Base64.decode(imgReferences[i], Base64.DEFAULT);
                itemImages[i] = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                this.images.setImageBitmap(itemImages[i]);

            } catch (Exception ex) {

                Log.e("Slideshow Activity", ex.getMessage(), ex);
                itemImages[i] = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_help);

            }
        }

    } //end void imgReferencesToBitmaps()

} //end class SlideshowActivity
