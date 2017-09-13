package com.github.eyers;

/**
 * Created by Nathan Shava on 29-Jul-17.
 * This class creates the SQLite database which is utilized across the app's activities to store & read data
 */

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.github.eyers.activities.NewCategoryInfo;
import com.github.eyers.activities.NewItemInfo;

import java.util.ArrayList;
import java.util.List;

public class EyeRSDatabaseHelper extends SQLiteOpenHelper {

    //CREATE ITEM TABLE QUERY
    private static final String CREATE_ITEM_TABLE_QUERY =
            "CREATE TABLE IF NOT EXISTS " + NewItemInfo.ItemInfo.TABLE_NAME
                    + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + NewItemInfo.ItemInfo.ITEM_NAME + " TEXT, "
                    + NewItemInfo.ItemInfo.ITEM_DESC + " TEXT);";

    //CREATE CATEGORY TABLE QUERY
    private static final String CREATE_CATEGORY_TABLE_QUERY =
            "CREATE TABLE IF NOT EXISTS " + NewCategoryInfo.CategoryInfo.TABLE_NAME
                    + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + NewCategoryInfo.CategoryInfo.CATEGORY_NAME + " TEXT, "
                    + NewCategoryInfo.CategoryInfo.CATEGORY_DESC + " TEXT);";

    //CREATE DEFAULT CATEGORIES
    private static final String INSERT_CATEGORY_BOOKS = //BOOKS
            "INSERT INTO " + NewCategoryInfo.CategoryInfo.TABLE_NAME +
                    "(category_name, category_desc) VALUES "
                    + "('BOOKS', 'Educational/Sci-Fi/Comics')";

    private static final String INSERT_CATEGORY_CLOTHES = //CLOTHES
            "INSERT INTO " + NewCategoryInfo.CategoryInfo.TABLE_NAME +
                    "(category_name, category_desc) VALUES "
                    + "('CLOTHES', 'Formal/Casual')";

    private static final String INSERT_CATEGORY_ACCESSORIES = //ACCESSORIES
            "INSERT INTO " + NewCategoryInfo.CategoryInfo.TABLE_NAME +
                    "(category_name, category_desc) VALUES "
                    + "('ACCESSORIES', 'Collectibles')";

    private static final String INSERT_CATEGORY_GAMES = //GAMES
            "INSERT INTO " + NewCategoryInfo.CategoryInfo.TABLE_NAME +
                    "(category_name, category_desc) VALUES "
                    + "('GAMES', 'Sport/Shooting/VR')";

    private static final String INSERT_CATEGORY_OTHER = //OTHER
            "INSERT INTO " + NewCategoryInfo.CategoryInfo.TABLE_NAME +
                    "(category_name, category_desc) VALUES "
                    + "('OTHER', 'Random stuff')";

    private static final String DB_NAME = "eyeRS.db"; //the name of the database
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

    //Creating the db
    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db, 0, DB_VERSION);
    }

    //Upgrading the db
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        updateMyDatabase(db, oldVersion, newVersion);
    }

    /**
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     * Method will handle any database updates that occur within the app after the user performs
     * specific actions
     */
    public void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion < 1) { //if the app is run for the first time

            try {

                //Create the Item table
                db.execSQL(CREATE_ITEM_TABLE_QUERY);
                //Display message in the logcat window after successful operation execution
                Log.e("DATABASE OPERATIONS", "...Item table created!");

                //Create the Category table
                db.execSQL(CREATE_CATEGORY_TABLE_QUERY);
                //Display message in the logcat window after successful operation execution
                Log.e("DATABASE OPERATIONS", "...Category table created!");

                //Insert the default categories
                db.execSQL(INSERT_CATEGORY_BOOKS); //Books default category
                db.execSQL(INSERT_CATEGORY_CLOTHES); //Clothes default category
                db.execSQL(INSERT_CATEGORY_ACCESSORIES); //Accessories default category
                db.execSQL(INSERT_CATEGORY_GAMES); //Games default category
                db.execSQL(INSERT_CATEGORY_OTHER); //Other default category

                //Display message in the logcat window after successful operation execution
                Log.e("DATABASE OPERATIONS", "...Default categories created successfully!");

            } catch (SQLException ex) {
                Log.e("DATABASE OPERATIONS", "...Unable to create categories!");
            }
        }
        if (newVersion > 0){ //any updates that occur within the app are handled here


        }

    } //end void updateMyDatabase()

    /**
     * SQL-select query to retrieve the category names.
     */
    public static final String GET_ALL_CATEGORIES =
            "SELECT " + NewCategoryInfo.CategoryInfo.CATEGORY_NAME + " FROM "
                    + NewCategoryInfo.CategoryInfo.TABLE_NAME + ";";

    //Return all categories in the db
    public List<String> getAllCategories() {

        List<String> categories = new ArrayList<String>();

        SQLiteDatabase db = getReadableDatabase();

        //Create a cursor to get the data from the db
        Cursor cursor = db.rawQuery(GET_ALL_CATEGORIES, null);

        // looping through all rows in the CATEGORIES table and adding to the list
        if (cursor.moveToFirst()) {

            do {

                categories.add(cursor.getString(0));

            } while (cursor.moveToNext());
        }

        // close the db connections
        cursor.close();
        db.close();

        return categories; //return the categories
    }

} //end class EyeRSDatabaseHelper
