package com.github.eyers;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.github.eyers.info.NewCategoryInfo;
import com.github.eyers.info.NewItemInfo;
import com.github.eyers.wrapper.ItemWrapper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Contains util methods and constants. Created by Matthew on 2017/06/19.
 *
 * @author Matthew Van der Bijl
 */
public final class EyeRS {

    /**
     *
     */
    public static final int REQUEST_READ_EXTERNAL_STORAGE = 123;
    /**
     *
     */
    public static final String PREFS_NAME = "EyeRS";
    /**
     *
     */
    public static EyeRS app;


    /**
     * @param context
     */
    public EyeRS(Context context) {
    }

    /**
     * @param password
     * @return
     * @throws RuntimeException No Such Algorithm Exception
     */
    public static String sha256(String password) throws RuntimeException {
        try {

            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] passHash = sha256.digest((password + "0c@RFe-5G47|GTN").getBytes());

            StringBuilder str = new StringBuilder();
            for (int i = 0; i < passHash.length; i++) {
                str.append(Integer.toString((passHash[i] & 0xff) + 0x100, 16).substring(1));
            }

            return str.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final AppCompatActivity context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(context, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(context, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions(context, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    /**
     * Method returns the categories result set of the SQL query and adds elements into a
     * list structure for the spinner.
     *
     * @return returns the list of categories
     */
    public static List<String> getCategoriesList(Activity activity) {

        List<String> addCategories = new ArrayList<String>();

        ContentResolver eyeRSContentResolver = activity.getContentResolver(); // Content resolver object

        String[] projection = {
                NewCategoryInfo.CategoryInfo.CATEGORY_ID,
                NewCategoryInfo.CategoryInfo.CATEGORY_NAME,
                NewCategoryInfo.CategoryInfo.CATEGORY_DESC,
                NewCategoryInfo.CategoryInfo.CATEGORY_ICON
        };

        Cursor cursor = eyeRSContentResolver.query(DBOperations.CONTENT_URI_CATEGORIES,
                projection, null, null, null);

        TreeSet<String> data = new TreeSet<>();

        if (cursor.moveToFirst()) {

            do {

                data.add(cursor.getString(1));

            } while (cursor.moveToNext());

            cursor.close();

        } else {
            if (activity != null) {
                Toast.makeText(activity, "No categories to load", Toast.LENGTH_LONG).show();
            }
        }

        for (String str : data) {

            addCategories.add(str);
        }

        return addCategories;
    }

    /**
     * Method to retrieve items from the db
     *
     * @return the items based on the selected category
     */
    public static ArrayList<ItemWrapper> getItems(String category, Activity activity) {

        ArrayList<ItemWrapper> items = new ArrayList<ItemWrapper>();

        ContentResolver eyeRSContentResolver = activity.getContentResolver(); // Content resolver object

        String[] projection = {
                NewItemInfo.ItemInfo.ITEM_ID,
                NewItemInfo.ItemInfo.CATEGORY_NAME,
                NewItemInfo.ItemInfo.ITEM_NAME,
                NewItemInfo.ItemInfo.ITEM_DESC,
                NewItemInfo.ItemInfo.ITEM_IMAGE
        };

        String[] selectionArgs = {};

        String whereClause = NewItemInfo.ItemInfo.CATEGORY_NAME + " = '" + category + "'";

        String sortOrder = NewItemInfo.ItemInfo.ITEM_NAME;

        Cursor cursor = eyeRSContentResolver.query(DBOperations.CONTENT_URI_ITEMS,
                projection, whereClause, null, sortOrder);

        if (cursor.moveToFirst()) {
            do {
                Bitmap decodedByte;
                try {
                    byte[] decodedString = Base64.decode(cursor.getString(4), Base64.DEFAULT);
                    decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                } catch (NullPointerException npe) {
                    Log.e("error loading image", npe.getMessage());
                    decodedByte = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_action_help);
                }
                items.add(new ItemWrapper(cursor.getString(2), decodedByte, cursor.getString(3)));
            } while (cursor.moveToNext());

            cursor.close();

        } else {
            if (activity != null) {
                Toast.makeText(activity, "Nothing to display.", Toast.LENGTH_SHORT).show();
            }
        }

        return items;

    }
}
