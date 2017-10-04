package com.github.eyers;

/**
 * Created by Nathan Shava on 29-Jul-17.
 * This class creates the SQLite database which is utilized across the app's activities to store & read data
 */

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.github.eyers.activities.NewCategoryInfo;
import com.github.eyers.activities.NewItemInfo;


public class EyeRSDatabaseHelper extends SQLiteOpenHelper {

    private ContentResolver eyeRSContentResolver;

    //CREATE ITEM TABLE QUERY
    private static final String CREATE_ITEM_TABLE_QUERY =
            "CREATE TABLE IF NOT EXISTS " + NewItemInfo.ItemInfo.TABLE_NAME
                    + NewItemInfo.ItemInfo.ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + NewItemInfo.ItemInfo.ITEM_NAME + " TEXT, "
                    + NewItemInfo.ItemInfo.ITEM_DESC + " TEXT);";

    //CREATE CATEGORY TABLE QUERY
    private static final String CREATE_CATEGORY_TABLE_QUERY =
            "CREATE TABLE IF NOT EXISTS " + NewCategoryInfo.CategoryInfo.TABLE_NAME
                    + NewCategoryInfo.CategoryInfo.CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + NewCategoryInfo.CategoryInfo.CATEGORY_NAME + " TEXT, "
                    + NewCategoryInfo.CategoryInfo.CATEGORY_DESC + " TEXT);";

    private static final String DB_NAME = "eyeRS.db"; //the name of the database
    private static final int DB_VERSION = 1; //the version of the database

    /**
     * We're calling the constructor of the SQLiteOpenHelper superclass,
     * and passing it the database name and version
     *
     * @param context
     */
    public EyeRSDatabaseHelper(Context context) {

        //the null parameter is an advanced feature relating to CursorFactory
        super(context, DB_NAME, null, DB_VERSION);

        eyeRSContentResolver = context.getContentResolver();

    } //end constructor

    /**
     * @param db Method is used to create the db
     *           It accepts a SQLite db object
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        try {

            //Create the Item table
            db.execSQL(CREATE_ITEM_TABLE_QUERY);
            //Display message in the logcat window after successful operation execution
            Log.e("DATABASE OPERATIONS", "...Item table created!");

            //Create the Category table
            db.execSQL(CREATE_CATEGORY_TABLE_QUERY);
            //Display message in the logcat window after successful operation execution
            Log.e("DATABASE OPERATIONS", "...Category table created!");

            insertDefaultCategoryBooks();
            insertDefaultCategoryClothes();
            insertDefaultCategoryAccessories();
            insertDefaultCategoryGames();
            insertDefaultCategoryOther();

            //Display message in the logcat window after successful operation execution
            Log.e("DATABASE OPERATIONS", "...Default categories created successfully!");

        } catch (SQLException ex) {

            Log.e("DATABASE OPERATIONS", "...Unable to create categories!");
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


    } //end void updateMyDatabase()

    /**
     * Method to insert the BOOKS default category
     */
    public void insertDefaultCategoryBooks() {

        // Defines an object to contain the new values to insert
        ContentValues bookValues = new ContentValues();

        /**
         * Sets the values of each column and inserts the word. The arguments to the "put"
         * method are "column name" and "value"
         */
        bookValues.put(NewCategoryInfo.CategoryInfo.CATEGORY_NAME, "BOOKS");
        bookValues.put(NewCategoryInfo.CategoryInfo.CATEGORY_DESC, "Educational/Sci-Fi/Comics");

        eyeRSContentResolver.insert(DbOperations.CONTENT_URI_CATEGORIES, bookValues);

    }

    /**
     * Method to insert the CLOTHES default category
     */
    public void insertDefaultCategoryClothes() {

        // Defines an object to contain the new values to insert
        ContentValues clothesValues = new ContentValues();

        /**
         * Sets the values of each column and inserts the word. The arguments to the "put"
         * method are "column name" and "value"
         */
        clothesValues.put(NewCategoryInfo.CategoryInfo.CATEGORY_NAME, "CLOTHES");
        clothesValues.put(NewCategoryInfo.CategoryInfo.CATEGORY_DESC, "Formal/Casual");

        eyeRSContentResolver.insert(DbOperations.CONTENT_URI_CATEGORIES, clothesValues);

    }

    /**
     * Method to insert the ACCESSORIES default category
     */
    public void insertDefaultCategoryAccessories() {

        // Defines an object to contain the new values to insert
        ContentValues accessoryValues = new ContentValues();

        /**
         * Sets the values of each column and inserts the word. The arguments to the "put"
         * method are "column name" and "value"
         */
        accessoryValues.put(NewCategoryInfo.CategoryInfo.CATEGORY_NAME, "ACCESSORIES");
        accessoryValues.put(NewCategoryInfo.CategoryInfo.CATEGORY_DESC, "Collectibles");

        eyeRSContentResolver.insert(DbOperations.CONTENT_URI_CATEGORIES, accessoryValues);

    }

    /**
     * Method to insert the GAMES default category
     */
    public void insertDefaultCategoryGames() {

        // Defines an object to contain the new values to insert
        ContentValues gameValues = new ContentValues();

        /**
         * Sets the values of each column and inserts the word. The arguments to the "put"
         * method are "column name" and "value"
         */
        gameValues.put(NewCategoryInfo.CategoryInfo.CATEGORY_NAME, "GAMES");
        gameValues.put(NewCategoryInfo.CategoryInfo.CATEGORY_DESC, "Sport/Shooting/VR");

        eyeRSContentResolver.insert(DbOperations.CONTENT_URI_CATEGORIES, gameValues);

    }

    /**
     * Method to insert the OTHER default category
     */
    public void insertDefaultCategoryOther() {

        // Defines an object to contain the new values to insert
        ContentValues otherValues = new ContentValues();

        /**
         * Sets the values of each column and inserts the word. The arguments to the "put"
         * method are "column name" and "value"
         */
        otherValues.put(NewCategoryInfo.CategoryInfo.CATEGORY_NAME, "OTHER");
        otherValues.put(NewCategoryInfo.CategoryInfo.CATEGORY_DESC, "Random stuff");

        eyeRSContentResolver.insert(DbOperations.CONTENT_URI_CATEGORIES, otherValues);

    }

} //end class EyeRSDatabaseHelper
