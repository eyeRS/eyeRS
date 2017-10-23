package com.github.eyers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.github.eyers.info.CategoryInfo;
import com.github.eyers.info.ItemInfo;
import com.github.eyers.info.UserRegistrationInfo;
import com.github.eyers.info.UserProfileInfo;

/**
 * A custom Content Provider to perform the database operations.
 * Created on 15-Sep-17
 *
 * @author Nathan Shava
 */

public class DBOperations extends ContentProvider {

    /**
     * Specify the Authority of the URI which has to be the package name.
     */
    public static final String AUTHORITY = "com.github.eyers.DBOperations";
    /**
     * Specify the table names to be used by the Content Provider.
     */
    public static final String CATEGORIES_TABLE = CategoryInfo.TABLE_NAME;
    public static final String ITEMS_TABLE = ItemInfo.TABLE_NAME;
    public static final String USER_REGISTRATION_TABLE = UserRegistrationInfo.TABLE_NAME;
    public static final String USER_PROFILE_TABLE = UserProfileInfo.TABLE_NAME;
    /**
     * Specify the table paths.
     */
    public static final String CATEGORIES_PATH = "/" + CATEGORIES_TABLE;
    public static final String ITEMS_PATH = "/" + ITEMS_TABLE;
    public static final String REGISTRATION_PATH = "/" + USER_REGISTRATION_TABLE;
    public static final String PROFILE_PATH = "/" + USER_PROFILE_TABLE;
    /**
     * A uri to do operations on the Categories table.
     * A content provider is identified by its uri.
     */
    public static final Uri CONTENT_URI_CATEGORIES = Uri.parse("content://" + AUTHORITY + CATEGORIES_PATH);
    public static final Uri CONTENT_URI_ITEMS = Uri.parse("content://" + AUTHORITY + ITEMS_PATH);
    public static final Uri CONTENT_URI_USER_REG = Uri.parse("content://" + AUTHORITY + REGISTRATION_PATH);
    public static final Uri CONTENT_URI_USER_PROFILE = Uri.parse("content://" + AUTHORITY + PROFILE_PATH);
    /**
     * Constants to identify the requested operation
     */
    public static final int ALL_CATEGORIES = 1;
    public static final int ALL_ITEMS = 2;
    public static final int REG_DETAILS = 3;
    public static final int PROFILE_DETAILS = 4;
    /**
     * The URI matcher maps to the specified table_name in the database.
     */
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    /**
     * Add the URIs for the respective db tables.
     */
    static {

        uriMatcher.addURI(AUTHORITY, CATEGORIES_TABLE, ALL_CATEGORIES);
        uriMatcher.addURI(AUTHORITY, ITEMS_TABLE, ALL_ITEMS);
        uriMatcher.addURI(AUTHORITY, USER_REGISTRATION_TABLE, REG_DETAILS);
        uriMatcher.addURI(AUTHORITY, USER_PROFILE_TABLE, PROFILE_DETAILS);

    }

    /**
     * This content provider does the database operations by this object
     */
    private EyeRSDatabaseHelper eyeRSDatabaseHelper;

    /**
     * System calls onCreate() when it starts up the provider
     */
    @Override
    public boolean onCreate() {

        //get access to the database helper
        eyeRSDatabaseHelper = new EyeRSDatabaseHelper(getContext());
        return true;
    }

    /**
     * @param uri
     * @return the MIME type corresponding to a content URI
     */
    @Override
    public String getType(Uri uri) {

        switch (uriMatcher.match(uri)) {

        }

        return null;
    }

    /**
     * Retrieve the table name to query based on the Content URI selected
     *
     * @return
     */
    public static String getTableName(Uri uri) {

        if (uri.equals(CONTENT_URI_CATEGORIES)) {
            return CATEGORIES_TABLE;
        }
        if (uri.equals(CONTENT_URI_ITEMS)) {
            return ITEMS_TABLE;
        }
        if (uri.equals(CONTENT_URI_USER_REG)) {
            return USER_REGISTRATION_TABLE;
        }
        if (uri.equals(CONTENT_URI_USER_PROFILE)) {
            return USER_PROFILE_TABLE;
        } else {
            return ("Table does not exist!");
        }
    }

    /**
     * The query() method must return a Cursor object, or if it fails, throw an Exception.
     * Using the SQLite database as the proposed data storage means we can simply return the Cursor returned
     * by one of the query() methods of the SQLite database class. If the query does not match any
     * rows, we should return a Cursor instance whose getCount() method returns 0.
     * We should return null only if an internal error occurred during the query process.
     */
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        int uriType = uriMatcher.match(uri);
        queryBuilder.setTables(getTableName(uri));

        switch (uriType) {

            case ALL_CATEGORIES:
                break;
            case ALL_ITEMS:
                break;
            case REG_DETAILS:
                break;
            case PROFILE_DETAILS:
                break;
            default:
                Log.e("Query Operation", "Unable to retrieve data");
        }

        Cursor cursor = queryBuilder.query(eyeRSDatabaseHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        /**
         * If we want to be notified of any changes
         */
        cursor.setNotificationUri(getContext().getContentResolver(),
                uri);
        return cursor;

    }

    /**
     * The insert() method adds a new row to the appropriate table, using the values in the
     * ContentValues argument.
     *
     * @param uri
     * @param values
     * @return
     */
    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        int uriType = uriMatcher.match(uri);
        String table = getTableName(uri);
        SQLiteDatabase db = eyeRSDatabaseHelper.getWritableDatabase();

        if (uriType == ALL_CATEGORIES){
            long categoryID = db.insert(table, null, values);
            getContext().getContentResolver().notifyChange(uri, null);
            return Uri.parse(table + "/" + categoryID);
        }
        if (uriType == ALL_ITEMS){
            long itemID = db.insert(table, null, values);
            getContext().getContentResolver().notifyChange(uri, null);
            return Uri.parse(table + "/" + itemID);
        }
        if (uriType == REG_DETAILS){
            long regID = db.insert(table, null, values);
            getContext().getContentResolver().notifyChange(uri, null);
            return Uri.parse(table + "/" + regID);
        }
        if (uriType == PROFILE_DETAILS){
            long profileID = db.insert(table, null, values);
            getContext().getContentResolver().notifyChange(uri, null);
            return Uri.parse(table + "/" + profileID);
        }
        else{
            Log.e("Insert operation", "Could not determine uri: " + uri);
            throw new UnsupportedOperationException("Unsupported URI: " + uri);
        }
    }

    /**
     * The delete() method deletes rows based on the selection or if an ID is provided then it
     * deletes a single row. The method returns the number of records deleted from the database.
     * If we choose not to delete the data physically then just update a flag here.
     *
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int uriType = uriMatcher.match(uri);
        SQLiteDatabase db = eyeRSDatabaseHelper.getWritableDatabase();
        int deletedRows = 0;

        switch (uriType) {

            case ALL_CATEGORIES:
                deletedRows = db.delete(CATEGORIES_TABLE,
                        selection,
                        selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return deletedRows;
    }

    /**
     * The update() method is similar to delete() where multiple rows are updated based on the selection
     * or a single row if the row ID is provided. The update method returns the number of updated
     * rows.
     *
     * @param uri
     * @param values
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) throws IllegalArgumentException {

        int uriType = uriMatcher.match(uri);
        SQLiteDatabase db = eyeRSDatabaseHelper.getWritableDatabase();
        int updatedRows = 0;

        switch (uriType) {

            case ALL_CATEGORIES:
                updatedRows = db.update(CATEGORIES_TABLE,
                        values,
                        selection,
                        selectionArgs);
                break;
            case REG_DETAILS:
                String username = uri.getLastPathSegment();

                if (TextUtils.isEmpty(selection)) {

                    updatedRows = db.update(USER_REGISTRATION_TABLE,
                            values,
                            UserRegistrationInfo.USER_NAME + " = " + username,
                            null);
                } else {

                    Log.e("Register Table", "Sorry could not update update the user credentials");
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return updatedRows;
    }

} //end class DBOperations

