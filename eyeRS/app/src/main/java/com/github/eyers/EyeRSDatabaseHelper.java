package com.github.eyers;

/**
 * Created by Nathan Shava on 29-Jul-17.
 */

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.github.eyers.activities.NewCategoryInfo;
import com.github.eyers.activities.NewItemInfo;

public class EyeRSDatabaseHelper extends SQLiteOpenHelper {

    //CREATE ITEM TABLE QUERY
    public static final String CREATE_ITEM_TABLE_QUERY =
            "CREATE TABLE IF NOT EXISTS " + NewItemInfo.ItemInfo.TABLE_NAME
                    + " (" + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + NewItemInfo.ItemInfo.ITEM_NAME + " TEXT, "
                    + NewItemInfo.ItemInfo.ITEM_DESC + " TEXT, "
                    + NewItemInfo.ItemInfo.DATE_ADDED + " DATE, "
                    + NewItemInfo.ItemInfo.ITEM_ICON + " BLOB);";
    //CREATE CATEGORY TABLE QUERY
    public static final String CREATE_CATEGORY_TABLE_QUERY =
            "CREATE TABLE IF NOT EXISTS " + NewCategoryInfo.CategoryInfo.TABLE_NAME
                    + " (" + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + NewCategoryInfo.CategoryInfo.CATEGORY_NAME + " TEXT, "
                    + NewCategoryInfo.CategoryInfo.CATEGORY_DESC + " TEXT, "
                    + NewCategoryInfo.CategoryInfo.CATEGORY_ICON + " BLOB);";
    //CREATE DEFAULT CATEGORIES
    public static final String CREATE_CATEGORY_BOOKS =
            "CREATE TABLE IF NOT EXISTS " + "BOOKS"
                    + " (" + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "book_title TEXT, "
                    + "book_desc TEXT, "
                    + "date_added TEXT, "
                    + "book_icon BLOB);";
    public static final String CREATE_CATEGORY_CLOTHES =
            "CREATE TABLE IF NOT EXISTS " + "CLOTHES"
                    + " (" + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "clothing_type TEXT, "
                    + "clothing_desc TEXT, "
                    + "date_added TEXT, "
                    + "clothing_icon BLOB);";
    public static final String CREATE_CATEGORY_ACCESSORIES =
            "CREATE TABLE IF NOT EXISTS " + "ACCESSORIES"
                    + " (" + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "accessory_name TEXT, "
                    + "accessory_desc TEXT, "
                    + "date_added TEXT, "
                    + "accessory_icon BLOB);";
    public static final String CREATE_CATEGORY_GAMES =
            "CREATE TABLE IF NOT EXISTS " + "GAMES"
                    + " (" + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "game_title TEXT, "
                    + "game_desc TEXT, "
                    + "date_added TEXT, "
                    + "game_icon BLOB);";
    public static final String CREATE_CATEGORY_OTHER =
            "CREATE TABLE IF NOT EXISTS " + "OTHER"
                    + " (" + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "other_title TEXT, "
                    + "other_desc TEXT, "
                    + "date_added TEXT, "
                    + "other_icon BLOB);";
    private static final String DB_NAME = "EYERS"; //the name of the database
    private static final int DB_VERSION = 1; //the version of the database

    //Constructor
    public EyeRSDatabaseHelper(Context context) {
        /*
            We're calling the constructor of the SQLiteOpenHelper superclass,
            and passing it the database name and version
         */

        //the null parameter is an advanced feature relating to CursorFactory
        super(context, DB_NAME, null, DB_VERSION);
        //Display message in the logcat window after successful operation execution
        Log.e("DATABASE OPERATIONS", "...Database created!");

    } //end constructor

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        updateMyDatabase(db, oldVersion, newVersion);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion <= 1) {

            try {

                db.beginTransaction();
                //Create the Item table
                db.execSQL(CREATE_ITEM_TABLE_QUERY);
                //Display message in the logcat window after successful operation execution
                Log.e("DATABASE OPERATIONS", "...Item table created!");

                //Create the Category table
                db.execSQL(CREATE_CATEGORY_TABLE_QUERY);
                //Display message in the logcat window after successful operation execution
                Log.e("DATABASE OPERATIONS", "...Category table created!");

                //Create the default categories
                db.execSQL(CREATE_CATEGORY_BOOKS); //Books default category
                db.execSQL(CREATE_CATEGORY_CLOTHES); //Clothes default category
                db.execSQL(CREATE_CATEGORY_ACCESSORIES); //Accessories default category
                db.execSQL(CREATE_CATEGORY_GAMES); //Games default category
                db.execSQL(CREATE_CATEGORY_OTHER); //Other default category

                db.close();

                //Display message in the logcat window after successful operation execution
                Log.e("DATABASE OPERATIONS", "...Default categories created successfully!");

            } catch (SQLException ex) {
                Log.e("DATABASE OPERATIONS", "...Unable to create categories!");
            }
            finally {
                db.endTransaction();
            }


        }
        if (oldVersion >= 2) {
            //Code to be executed when eyers db is updated to a higher version
        }
    } //end void updateMyDatabase()

} //end class EyeRSDatabaseHelper
