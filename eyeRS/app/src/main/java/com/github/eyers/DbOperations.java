package com.github.eyers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Nathan Shava on 15-Sep-17.
 * A custom Content Provider to do the database operations
 */

public class DbOperations extends ContentProvider {


    public static final String PROVIDER_NAME = "com.github.eyers.dboperations";

    /**
     * A uri to do operations on the Categories table.
     * A content provider is identified by its uri.
     */
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/categories");

    /**
     * Constants to identify the requested operation
     */
    private static final int CATEGORIES = 1;
    private static final UriMatcher uriMatcher;

    static {

        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "categories", CATEGORIES);
    }

    /**
     * This content provider does the database operations by this object
     */
    EyeRSDatabaseHelper eyeRSDatabaseHelper;

    /**
     * A callback method which is invoked when the content provider is starting up
     */
    @Override
    public boolean onCreate() {
        eyeRSDatabaseHelper = new EyeRSDatabaseHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    /**
     * A callback method which is by the default content uri
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        if (uriMatcher.match(uri) == CATEGORIES) {
            return eyeRSDatabaseHelper.getAllCategories();
        } else {
            return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO Auto-generated method stub
        return 0;
    }

}

