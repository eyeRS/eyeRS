package com.github.eyers;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.github.eyers.activities.NewCategoryInfo;
import com.github.eyers.activities.NewItemInfo;
import com.github.eyers.activities.UserRegInfo;

/**
 * Created by Nathan Shava on 29-Jul-17
 * The Database helper class performs the default DDL & DML operations for the app
 */
public class EyeRSDatabaseHelper extends SQLiteOpenHelper {

    /**
     * Content resolver object
     */
    private ContentResolver eyeRSContentResolver;

    /**
     * CREATE ITEM TABLE QUERY
     */
    private static final String CREATE_ITEM_TABLE_QUERY =
            "CREATE TABLE IF NOT EXISTS " + NewItemInfo.ItemInfo.TABLE_NAME
                    + NewItemInfo.ItemInfo.ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + NewItemInfo.ItemInfo.ITEM_NAME + " TEXT, "
                    + NewItemInfo.ItemInfo.ITEM_DESC + " TEXT);";

    /**
     * CREATE CATEGORY TABLE QUERY
     */
    private static final String CREATE_CATEGORY_TABLE_QUERY =
            "CREATE TABLE IF NOT EXISTS " + NewCategoryInfo.CategoryInfo.TABLE_NAME
                    + NewCategoryInfo.CategoryInfo.CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + NewCategoryInfo.CategoryInfo.CATEGORY_NAME + " TEXT, "
                    + NewCategoryInfo.CategoryInfo.CATEGORY_DESC + " TEXT);";

    /**
     * CREATE USER REGISTRATION TABLE QUERY
     */
    private static final String CREATE_USER_REGISTRATION_TABLE_QUERY =
            "CREATE TABLE IF NOT EXISTS " + UserRegInfo.RegInfo.TABLE_NAME
                    + UserRegInfo.RegInfo.REG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + UserRegInfo.RegInfo.USER_NAME + " TEXT, "
                    + UserRegInfo.RegInfo.USER_EMAIL + " TEXT, "
                    + UserRegInfo.RegInfo.USER_PIN + " TEXT, "
                    + UserRegInfo.RegInfo.SECURITY_QUESTION + " TEXT, "
                    + UserRegInfo.RegInfo.SECURITY_RESPONSE + " TEXT);";

    private static final String DB_NAME = "eyeRS.db"; //db name
    private static final int DB_VERSION = 1; //db version

    /**
     * We're calling the constructor of the SQLiteOpenHelper superclass,
     * and passing it the database name and version
     *
     * @param context
     */
    public EyeRSDatabaseHelper(Context context) {

        /**
         * The null parameter is an advanced feature relating to CursorFactory
         */
        super(context, DB_NAME, null, DB_VERSION);
        eyeRSContentResolver = context.getContentResolver();

    }

    /**
     * @param db Method is used to create the db tables
     *           It accepts a SQLite db object
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        try {

            /**
             * Creates the Item table
             */
            db.execSQL(CREATE_ITEM_TABLE_QUERY);
            Log.e("DATABASE OPERATIONS", "...Item table created!");

            /**
             * Creates the Category table
             */
            db.execSQL(CREATE_CATEGORY_TABLE_QUERY);
            Log.e("DATABASE OPERATIONS", "...Category table created!");

            /**
             * Creates the User Registration table
             */
            db.execSQL(CREATE_USER_REGISTRATION_TABLE_QUERY);
            Log.e("DATABASE OPERATIONS", "...User Registration table created!");

            insertDefaultCategoryBooks();
            insertDefaultCategoryClothes();
            insertDefaultCategoryAccessories();
            insertDefaultCategoryGames();
            insertDefaultCategoryOther();
            Log.e("DATABASE OPERATIONS", "...Default categories created successfully!");

        } catch (SQLException ex) {

            Log.e("DATABASE OPERATIONS", "Unable to perform default DDL & DML operations!");
        }
    }

    /**
     * @param db
     * @param oldVersion
     * @param newVersion Method is used to upgrade the db
     *                   It accepts a SQLite db object the old and new versions of the db to validate whether an
     *                   upgrade is due
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        updateMyDatabase(db, oldVersion, newVersion);
    }

    /**
     * @param db
     * @param oldVersion
     * @param newVersion Method will handle any database updates that occur within the app after the user performs
     *                   specific actions
     */
    public void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {


    }

    /**
     * Method to insert the BOOKS default category
     */
    public void insertDefaultCategoryBooks() {

        /**
         * Define an object to contain the new values to insert
         */
        ContentValues bookValues = new ContentValues();

        bookValues.put(NewCategoryInfo.CategoryInfo.CATEGORY_NAME, "BOOKS");
        bookValues.put(NewCategoryInfo.CategoryInfo.CATEGORY_DESC, "Educational/Sci-Fi/Comics");

        /**
         * Content resolver insert operation
         */
        eyeRSContentResolver.insert(DbOperations.CONTENT_URI_CATEGORIES, bookValues);

    }

    /**
     * Method to insert the CLOTHES default category
     */
    public void insertDefaultCategoryClothes() {

        /**
         * Define an object to contain the new values to insert
         */
        ContentValues clothesValues = new ContentValues();

        clothesValues.put(NewCategoryInfo.CategoryInfo.CATEGORY_NAME, "CLOTHES");
        clothesValues.put(NewCategoryInfo.CategoryInfo.CATEGORY_DESC, "Formal/Casual");

        /**
         * Content resolver insert operation
         */
        eyeRSContentResolver.insert(DbOperations.CONTENT_URI_CATEGORIES, clothesValues);

    }

    /**
     * Method to insert the ACCESSORIES default category
     */
    public void insertDefaultCategoryAccessories() {

        /**
         * Define an object to contain the new values to insert
         */
        ContentValues accessoryValues = new ContentValues();

        accessoryValues.put(NewCategoryInfo.CategoryInfo.CATEGORY_NAME, "ACCESSORIES");
        accessoryValues.put(NewCategoryInfo.CategoryInfo.CATEGORY_DESC, "Collectibles");

        /**
         * Content resolver insert operation
         */
        eyeRSContentResolver.insert(DbOperations.CONTENT_URI_CATEGORIES, accessoryValues);

    }

    /**
     * Method to insert the GAMES default category
     */
    public void insertDefaultCategoryGames() {

        /**
         * Define an object to contain the new values to insert
         */
        ContentValues gameValues = new ContentValues();

        gameValues.put(NewCategoryInfo.CategoryInfo.CATEGORY_NAME, "GAMES");
        gameValues.put(NewCategoryInfo.CategoryInfo.CATEGORY_DESC, "Sport/Shooting/VR");

        /**
         * Content resolver insert operation
         */
        eyeRSContentResolver.insert(DbOperations.CONTENT_URI_CATEGORIES, gameValues);

    }

    /**
     * Method to insert the OTHER default category
     */
    public void insertDefaultCategoryOther() {

        /**
         * Define an object to contain the new values to insert
         */
        ContentValues otherValues = new ContentValues();

        otherValues.put(NewCategoryInfo.CategoryInfo.CATEGORY_NAME, "OTHER");
        otherValues.put(NewCategoryInfo.CategoryInfo.CATEGORY_DESC, "Random stuff");

        /**
         * Content resolver insert operation
         */
        eyeRSContentResolver.insert(DbOperations.CONTENT_URI_CATEGORIES, otherValues);

    }

} //end class EyeRSDatabaseHelper
