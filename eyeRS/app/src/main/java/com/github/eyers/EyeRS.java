package com.github.eyers;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
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

import com.badlogic.gdx.backends.android.AndroidPreferences;
import com.github.eyers.info.CategoryInfo;
import com.github.eyers.info.ItemInfo;
import com.github.eyers.wrapper.ItemWrapper;

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
     * User prefrences. Initilised when the login activity is created.
     */
    public static AndroidPreferences PREFERENCES;

    @Deprecated
    private EyeRS() {
    }

//    02/11/2017 Matthew: No longer used?
//    /**
//     * @param password
//     * @return
//     * @throws RuntimeException No Such Algorithm Exception
//     */
//    public static String sha256(String password) throws RuntimeException {
//        try {
//
//            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
//            byte[] passHash = sha256.digest((password + "0c@RFe-5G47|GTN").getBytes());
//
//            StringBuilder str = new StringBuilder();
//            for (int i = 0; i < passHash.length; i++) {
//                str.append(Integer.toString((passHash[i] & 0xff) + 0x100, 16).substring(1));
//            }
//
//            return str.toString();
//        } catch (NoSuchAlgorithmException ex) {
//            throw new RuntimeException(ex);
//        }
//    }

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
    public static List<ItemLabel> getCategoriesList(Activity activity) {

        List<ItemLabel> addCategories = new ArrayList<>();

        ContentResolver eyeRSContentResolver = activity.getContentResolver(); // Content resolver object

        String[] projection = {
                CategoryInfo.CATEGORY_ID,
                CategoryInfo.CATEGORY_NAME,
                CategoryInfo.CATEGORY_ICON
        };

        String whereClause = "";
        String[] whereArgs = {};
        String sortOrder = CategoryInfo.CATEGORY_NAME;

        Cursor cursor = eyeRSContentResolver.query(
                DBOperations.CONTENT_URI_CATEGORIES,
                projection,
                whereClause,
                whereArgs,
                sortOrder);

        TreeSet<ItemLabel> data = new TreeSet<>();

        if (cursor.moveToFirst()) {

            do {
                Bitmap decodedByte = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_action_help);
                try {
                    String img = cursor.getString(
                            cursor.getColumnIndex(CategoryInfo.CATEGORY_ICON));

                    byte[] decodedString = Base64.decode(img, Base64.DEFAULT);
                    decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                } catch (Throwable t) { // lets be sure we get it
                    Log.w("Error loading image",
                            cursor.getString(
                                    cursor.getColumnIndex(CategoryInfo.CATEGORY_NAME))
                                    + " "
                                    + t.getMessage(), t);

                    byte[] decodedString = Base64.decode("", Base64.DEFAULT);
                    decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                }

                data.add(new ItemLabel(cursor.getString(
                        cursor.getColumnIndex(CategoryInfo.CATEGORY_NAME)),
                        decodedByte,
                        ""));

            } while (cursor.moveToNext());

            cursor.close();

        } else {
            if (activity == null) {
                Toast.makeText(activity, "No categories to load", Toast.LENGTH_LONG).show();
            }
        }

        for (ItemLabel str : data) {
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
                ItemInfo.ITEM_ID,
                ItemInfo.CATEGORY_NAME,
                ItemInfo.ITEM_NAME,
                ItemInfo.ITEM_DESC,
                ItemInfo.ITEM_IMAGE
        };

        String whereClause = ItemInfo.CATEGORY_NAME + " = '" + category + "'";
        String[] whereArgs = {};
        String sortOrder = ItemInfo.ITEM_NAME;

        Cursor cursor = eyeRSContentResolver.query(
                DBOperations.CONTENT_URI_ITEMS,
                projection,
                whereClause,
                whereArgs,
                sortOrder);

        if (cursor.moveToFirst()) {
            do {

                Bitmap decodedByte;
                try {

                    byte[] decodedString = Base64.decode(cursor.getString(
                            cursor.getColumnIndex(ItemInfo.ITEM_IMAGE)), Base64.DEFAULT);
                    decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                } catch (NullPointerException npe) {

                    Log.e("error loading image", npe.getMessage());
                    decodedByte = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_action_help);
                }
                items.add(new ItemWrapper(

                        cursor.getString(cursor.getColumnIndex(ItemInfo.ITEM_NAME)),
                        decodedByte,
                        cursor.getString(cursor.getColumnIndex(ItemInfo.ITEM_DESC))));

            } while (cursor.moveToNext());

            cursor.close();

        } else {
            if (activity == null) {

                Toast.makeText(activity, "Nothing to display.", Toast.LENGTH_SHORT).show();
            }
        }
        return items;
    }
}
