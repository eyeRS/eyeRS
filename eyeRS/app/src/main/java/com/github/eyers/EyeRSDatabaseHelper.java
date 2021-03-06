package com.github.eyers;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.github.eyers.info.CategoryInfo;
import com.github.eyers.info.ItemInfo;
import com.github.eyers.info.UserRegistrationInfo;

import java.lang.reflect.Field;

/**
 * The Database helper class performs the default DDL & DML operations for the app.
 * Created on 29-Jul-17
 *
 * @author Nathan Shava
 * @see SQLiteOpenHelper
 * @see DBOperations
 */
public class EyeRSDatabaseHelper extends SQLiteOpenHelper {

    /**
     * CREATE ITEM TABLE QUERY.
     */
    private static final String CREATE_ITEM_TABLE_QUERY =
            "CREATE TABLE IF NOT EXISTS " + ItemInfo.TABLE_NAME + " ( "
                    + ItemInfo.ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + ItemInfo.CATEGORY_NAME + " TEXT, "
                    + ItemInfo.ITEM_NAME + " TEXT, "
                    + ItemInfo.ITEM_DESC + " TEXT, "
                    + ItemInfo.ITEM_IMAGE + " TEXT);";
    /**
     * CREATE CATEGORY TABLE QUERY.
     */
    private static final String CREATE_CATEGORY_TABLE_QUERY =
            "CREATE TABLE IF NOT EXISTS " + CategoryInfo.TABLE_NAME + " ( "
                    + CategoryInfo.CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + CategoryInfo.CATEGORY_NAME + " TEXT);";
    /**
     * CREATE USER REGISTRATION TABLE QUERY.
     */
    private static final String CREATE_USER_REGISTRATION_TABLE_QUERY =
            "CREATE TABLE IF NOT EXISTS " + UserRegistrationInfo.TABLE_NAME + " ( "
                    + UserRegistrationInfo.REG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + UserRegistrationInfo.USER_NAME + " TEXT, "
                    + UserRegistrationInfo.EMAIL_ADD + " TEXT, "
                    + UserRegistrationInfo.USER_PIN + " TEXT, "
                    + UserRegistrationInfo.SECURITY_QUESTION + " TEXT, "
                    + UserRegistrationInfo.SECURITY_RESPONSE + " TEXT);";

    // DB variables
    private static final String DB_NAME = "eyeRS.db";
    private static final int DB_VERSION = 1;

    /**
     * We're calling the constructor of the SQLiteOpenHelper superclass,
     * and passing it the database name and version.
     *
     * @param context
     */
    public EyeRSDatabaseHelper(Context context) {

        /*
         * The null parameter is an advanced feature relating to CursorFactory.
         */
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * Method is used to create the db tables. It accepts a SQLite db object.
     *
     * @param db the acsepted SQLite object
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            /*
             * Creates the Item table
             */
            db.execSQL(CREATE_ITEM_TABLE_QUERY);
            Log.e("DATABASE OPERATIONS", "...Item table created!");
            /*
             * Creates the Category table
             */
            db.execSQL(CREATE_CATEGORY_TABLE_QUERY);
            Log.e("DATABASE OPERATIONS", "...Category table created!");
            /*
             * Creates the User Registration table
             */
            db.execSQL(CREATE_USER_REGISTRATION_TABLE_QUERY);
            Log.e("DATABASE OPERATIONS", "...User Registration table created!");

            insertDefaultCategoryBooks(db);
            insertDefaultCategoryClothes(db);
            insertDefaultCategoryAccessories(db);
            insertDefaultCategoryGames(db);
            insertDefaultCategoryOther(db);

            Log.e("DATABASE OPERATIONS", "...Default categories created successfully!");

        } catch (SQLException ex) {
            Log.e("DATABASE OPERATIONS", "Unable to perform default DDL & DML operations!", ex);
        }
    }

    /**
     * Method is used to upgrade the db It accepts a SQLite db object the old and new versions of the
     * db to validate whether an upgrade is due
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Method to insert the BOOKS default category.
     */
    private void insertDefaultCategoryBooks(SQLiteDatabase db) {

        /*
         * Define an object to contain the new values to insert
         */
        ContentValues bookValues = new ContentValues();

        bookValues.put(CategoryInfo.CATEGORY_NAME, "BOOKS");

        /*
         * Content resolver insert operation
         */
        db.insert(CategoryInfo.TABLE_NAME, null, bookValues);
    }

    /**
     * Method to insert the CLOTHES default category.
     */
    private void insertDefaultCategoryClothes(SQLiteDatabase db) {

        /*
         * Define an object to contain the new values to insert
         */
        ContentValues clothesValues = new ContentValues();

        clothesValues.put(CategoryInfo.CATEGORY_NAME, "CLOTHES");

        /*
         * Content resolver insert operation
         */
        db.insert(CategoryInfo.TABLE_NAME, null, clothesValues);

    }

    /**
     * Method to insert the ACCESSORIES default category
     */
    private void insertDefaultCategoryAccessories(SQLiteDatabase db) {

        /*
         * Define an object to contain the new values to insert
         */
        ContentValues accessoryValues = new ContentValues();

        accessoryValues.put(CategoryInfo.CATEGORY_NAME, "ACCESSORIES");

        /*
         * Content resolver insert operation
         */
        db.insert(CategoryInfo.TABLE_NAME, null, accessoryValues);

    }

    /**
     * Method to insert the GAMES default category.
     */
    private void insertDefaultCategoryGames(SQLiteDatabase db) {

        /*
         * Define an object to contain the new values to insert
         */
        ContentValues gameValues = new ContentValues();

        gameValues.put(CategoryInfo.CATEGORY_NAME, "GAMES");

        /*
         * Content resolver insert operation
         */
        db.insert(CategoryInfo.TABLE_NAME, null, gameValues);

    }

    /**
     * Method to insert the OTHER default category.
     */
    private void insertDefaultCategoryOther(SQLiteDatabase db) {

        /*
         * Define an object to contain the new values to insert
         */
        ContentValues otherValues = new ContentValues();

        otherValues.put(CategoryInfo.CATEGORY_NAME, "OTHER");

        /*
         * Content resolver insert operation
         */
        db.insert(CategoryInfo.TABLE_NAME, null, otherValues);
    }

} //end class EyeRSDatabaseHelper