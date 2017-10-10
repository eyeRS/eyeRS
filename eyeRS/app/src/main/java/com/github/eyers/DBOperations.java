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
import android.widget.Toast;

import com.github.eyers.activities.NewCategoryInfo;
import com.github.eyers.activities.NewItemInfo;
import com.github.eyers.activities.UserRegInfo;

/**
 * A custom Content Provider to perform the database operations. Created by Nathan Shava on 15-Sep-17.
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
    public static final String CATEGORIES_TABLE = NewCategoryInfo.CategoryInfo.TABLE_NAME;
    public static final String ITEMS_TABLE = NewItemInfo.ItemInfo.TABLE_NAME;
    public static final String USER_REGISTRATION_TABLE = UserRegInfo.RegInfo.TABLE_NAME;
    /**
     * Specify the table paths.
     */
    public static final String CATEGORIES_PATH = "/" + CATEGORIES_TABLE;
    public static final String ITEMS_PATH = "/" + ITEMS_TABLE;
    public static final String REGISTRATION_PATH = "/" + USER_REGISTRATION_TABLE;
    /**
     * A uri to do operations on the Categories table.
     * A content provider is identified by its uri.
     */
    public static final Uri CONTENT_URI_CATEGORIES = Uri.parse("content://" + AUTHORITY + CATEGORIES_PATH);
    public static final Uri CONTENT_URI_ITEMS = Uri.parse("content://" + AUTHORITY + ITEMS_PATH);
    public static final Uri CONTENT_URI_USER_REG = Uri.parse("content://" + AUTHORITY + REGISTRATION_PATH);
    /**
     * Constants to identify the requested operation
     */
    public static final int ALL_CATEGORIES = 1;
    public static final int ALL_ITEMS = 2;
    public static final int REG_DETAILS = 3;
    public static final int CATEGORIES_ID = 4;
    public static final int CATEGORIES_SPECIFIC_NAME = 5;
    public static final int ITEMS_SPECIFIC_NAME = 6;
    public static final int COUNT_CATEGORIES = 7;
    public static final int COUNT_ITEMS = 8;
    /**
     * The URI matcher maps to the specified table_name in the database.
     */
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    /**
     * Add the URIs for the respective db tables.
     */
    static {

        uriMatcher.addURI(AUTHORITY, CATEGORIES_TABLE, ALL_CATEGORIES);
        uriMatcher.addURI(AUTHORITY, CATEGORIES_TABLE + "/#", CATEGORIES_ID);
        uriMatcher.addURI(AUTHORITY, ITEMS_TABLE, ALL_ITEMS);
        uriMatcher.addURI(AUTHORITY, ITEMS_TABLE, ITEMS_SPECIFIC_NAME);
        uriMatcher.addURI(AUTHORITY, USER_REGISTRATION_TABLE, REG_DETAILS);
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
        return false;
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
        boolean useAuthorityUri = false;

        switch (uriType) {

            case CATEGORIES_ID:
                queryBuilder.setTables(CATEGORIES_TABLE);
                queryBuilder.appendWhere(NewCategoryInfo.CategoryInfo.CATEGORY_ID + " = "
                        + uri.getLastPathSegment());
                break;
            case ALL_CATEGORIES:
                queryBuilder.setTables(CATEGORIES_TABLE);
                break;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
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
        if (useAuthorityUri) {

            cursor.setNotificationUri(getContext().getContentResolver(),
                    CONTENT_URI_CATEGORIES);
        } else {
            cursor.setNotificationUri(getContext().getContentResolver(),
                    uri);
        }

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

        SQLiteDatabase db = eyeRSDatabaseHelper.getWritableDatabase();

        long id = 0;
        switch (uriType) {
            case ALL_CATEGORIES:
                id = db.insert(CATEGORIES_TABLE, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return Uri.parse(CATEGORIES_TABLE + "/" + id);
            case REG_DETAILS:
                id = db.insert(USER_REGISTRATION_TABLE, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return Uri.parse(USER_REGISTRATION_TABLE + "/" + id);
            case ALL_ITEMS:
                id = db.insert(ITEMS_TABLE, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return Uri.parse(ITEMS_TABLE + "/" + id);
            default:
                Toast.makeText(null, "Sorry could not perform add operation", Toast.LENGTH_SHORT).show();
                return null;
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

            case CATEGORIES_ID:

                String id = uri.getLastPathSegment();

                if (TextUtils.isEmpty(selection)) {
                    deletedRows = db.delete(CATEGORIES_TABLE,
                            NewCategoryInfo.CategoryInfo.CATEGORY_ID + "=" + id,
                            null);
                } else {
                    deletedRows = db.delete(CATEGORIES_TABLE,
                            NewCategoryInfo.CategoryInfo.CATEGORY_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
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
            case CATEGORIES_ID:
                String id = uri.getLastPathSegment();

                if (TextUtils.isEmpty(selection)) {

                    updatedRows = db.update(CATEGORIES_TABLE,
                            values,
                            NewCategoryInfo.CategoryInfo.CATEGORY_ID + " = " + id,
                            null);
                } else {

                    updatedRows = db.update(CATEGORIES_TABLE,
                            values,
                            NewCategoryInfo.CategoryInfo.CATEGORY_ID + " = " + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            case REG_DETAILS:
                String username = uri.getLastPathSegment();

                if (TextUtils.isEmpty(selection)) {

                    updatedRows = db.update(USER_REGISTRATION_TABLE,
                            values,
                            UserRegInfo.RegInfo.USER_NAME + " = " + username,
                            null);
                } else {

                    updatedRows = db.update(USER_REGISTRATION_TABLE,
                            values,
                            UserRegInfo.RegInfo.USER_NAME + " = " + username,
                            null);
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return updatedRows;
    }

}

