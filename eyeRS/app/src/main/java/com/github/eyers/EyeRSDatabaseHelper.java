package com.github.eyers;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.github.eyers.activities.UserProfileInfo;
import com.github.eyers.info.NewCategoryInfo;
import com.github.eyers.info.NewItemInfo;
import com.github.eyers.info.NewRegInfo;

import java.lang.reflect.Field;

/**
 * The Database helper class performs the default DDL & DML operations for the app.
 * Created on 29-Jul-17
 *
 * @author Nathan Shava
 */
public class EyeRSDatabaseHelper extends SQLiteOpenHelper {

    /**
     * CREATE ITEM TABLE QUERY.
     */
    private static final String CREATE_ITEM_TABLE_QUERY =
            "CREATE TABLE IF NOT EXISTS " + NewItemInfo.ItemInfo.TABLE_NAME + " ( "
                    + NewItemInfo.ItemInfo.ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + NewItemInfo.ItemInfo.CATEGORY_NAME + " TEXT, "
                    + NewItemInfo.ItemInfo.ITEM_NAME + " TEXT, "
                    + NewItemInfo.ItemInfo.ITEM_DESC + " TEXT, "
                    + NewItemInfo.ItemInfo.ITEM_IMAGE + " TEXT);";
    /**
     * CREATE CATEGORY TABLE QUERY.
     */
    private static final String CREATE_CATEGORY_TABLE_QUERY =
            "CREATE TABLE IF NOT EXISTS " + NewCategoryInfo.CategoryInfo.TABLE_NAME + " ( "
                    + NewCategoryInfo.CategoryInfo.CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + NewCategoryInfo.CategoryInfo.CATEGORY_NAME + " TEXT, "
                    + NewCategoryInfo.CategoryInfo.CATEGORY_DESC + " TEXT, "
                    + NewCategoryInfo.CategoryInfo.CATEGORY_ICON + " TEXT);";
    /**
     * CREATE USER REGISTRATION TABLE QUERY.
     */
    private static final String CREATE_USER_REGISTRATION_TABLE_QUERY =
            "CREATE TABLE IF NOT EXISTS " + NewRegInfo.UserRegistrationInfo.TABLE_NAME + " ( "
                    + NewRegInfo.UserRegistrationInfo.REG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + NewRegInfo.UserRegistrationInfo.USER_NAME + " TEXT, "
                    + NewRegInfo.UserRegistrationInfo.EMAIL_ADD + " TEXT, "
                    + NewRegInfo.UserRegistrationInfo.USER_PIN + " TEXT, "
                    + NewRegInfo.UserRegistrationInfo.SECURITY_QUESTION + " TEXT, "
                    + NewRegInfo.UserRegistrationInfo.SECURITY_RESPONSE + " TEXT);";
    /**
     * CREATE USER PROFILE TABLE QUERY.
     */
    private static final String CREATE_USER_PROFILE_TABLE_QUERY =
            "CREATE TABLE IF NOT EXISTS " + UserProfileInfo.ProfileInfo.TABLE_NAME + " ( "
                    + UserProfileInfo.ProfileInfo.PROFILE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + UserProfileInfo.ProfileInfo.USER_NAME + " TEXT, "
                    + UserProfileInfo.ProfileInfo.USER_AVATAR + " TEXT);";

    /**
     * DB variables
     */
    private static final String DB_NAME = "eyeRS.db";
    private static final int DB_VERSION = 1;
    /**
     * Content resolver object.
     */
    private ContentResolver eyeRSContentResolver;

    /**
     * We're calling the constructor of the SQLiteOpenHelper superclass,
     * and passing it the database name and version.
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
            /**
             * Creates the Profile Settings table
             */
            db.execSQL(CREATE_USER_PROFILE_TABLE_QUERY);
            Log.e("DATABASE OPERATIONS", "...Profile Settings table created!");

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
     * @param db
     * @param oldVersion
     * @param newVersion Method is used to upgrade the db
     *                   It accepts a SQLite db object the old and new versions of the db to validate whether an
     *                   upgrade is due
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Method to insert the BOOKS default category.
     */
    public void insertDefaultCategoryBooks(SQLiteDatabase db) {

        /**
         * Define an object to contain the new values to insert
         */
        ContentValues bookValues = new ContentValues();

        String bookIcon = "";
        Field field;

        try {

            field = R.drawable.class.getField("ic_white_book");
            bookIcon = field.toString();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        bookValues.put(NewCategoryInfo.CategoryInfo.CATEGORY_NAME, "BOOKS");
        bookValues.put(NewCategoryInfo.CategoryInfo.CATEGORY_DESC, "Educational/Sci-Fi/Comics");
        bookValues.put(NewCategoryInfo.CategoryInfo.CATEGORY_ICON, bookIcon);


        /**
         * Content resolver insert operation
         */
        db.insert(NewCategoryInfo.CategoryInfo.TABLE_NAME, null, bookValues);
    }

    /**
     * Method to insert the CLOTHES default category.
     */
    public void insertDefaultCategoryClothes(SQLiteDatabase db) {

        /**
         * Define an object to contain the new values to insert
         */
        ContentValues clothesValues = new ContentValues();

        String clothingIcon = "";
        Field field;

        try {

            field = R.drawable.class.getField("ic_polo_shirt");
            clothingIcon = field.toString();
        } catch (Exception ex) {
            Log.e("Insert Error", ex.getMessage());
        }

        clothesValues.put(NewCategoryInfo.CategoryInfo.CATEGORY_NAME, "CLOTHES");
        clothesValues.put(NewCategoryInfo.CategoryInfo.CATEGORY_DESC, "Formal/Casual");
        clothesValues.put(NewCategoryInfo.CategoryInfo.CATEGORY_ICON, clothingIcon);

        /**
         * Content resolver insert operation
         */
        db.insert(NewCategoryInfo.CategoryInfo.TABLE_NAME, null, clothesValues);

    }

    /**
     * Method to insert the ACCESSORIES default category
     */
    public void insertDefaultCategoryAccessories(SQLiteDatabase db) {

        /**
         * Define an object to contain the new values to insert
         */
        ContentValues accessoryValues = new ContentValues();

        String accessoryIcon = "";
        Field field;

        try {
            field = R.drawable.class.getField("ic_swap_bag");
            accessoryIcon = field.toString();
        } catch (Exception ex) {
            Log.e("Insert Error", ex.getMessage());
        }

        accessoryValues.put(NewCategoryInfo.CategoryInfo.CATEGORY_NAME, "ACCESSORIES");
        accessoryValues.put(NewCategoryInfo.CategoryInfo.CATEGORY_DESC, "Collectibles");
        accessoryValues.put(NewCategoryInfo.CategoryInfo.CATEGORY_ICON, accessoryIcon);

        /**
         * Content resolver insert operation
         */
        db.insert(NewCategoryInfo.CategoryInfo.TABLE_NAME, null, accessoryValues);

    }

    /**
     * Method to insert the GAMES default category.
     */
    public void insertDefaultCategoryGames(SQLiteDatabase db) {

        /**
         * Define an object to contain the new values to insert
         */
        ContentValues gameValues = new ContentValues();

        String gameIcon = "";
        Field field;

        try {
            field = R.drawable.class.getField("ic_retro_controller");
            gameIcon = field.toString();
        } catch (Exception ex) {
            Log.e("Insert Error", ex.getMessage());
        }


        gameValues.put(NewCategoryInfo.CategoryInfo.CATEGORY_NAME, "GAMES");
        gameValues.put(NewCategoryInfo.CategoryInfo.CATEGORY_DESC, "Sport/Shooting/VR");
        gameValues.put(NewCategoryInfo.CategoryInfo.CATEGORY_ICON, gameIcon);

        /**
         * Content resolver insert operation
         */
        db.insert(NewCategoryInfo.CategoryInfo.TABLE_NAME, null, gameValues);

    }

    /**
     * Method to insert the OTHER default category.
     */
    public void insertDefaultCategoryOther(SQLiteDatabase db) {

        /**
         * Define an object to contain the new values to insert
         */
        ContentValues otherValues = new ContentValues();

        String otherIcon = "";
        Field field;
        try {
            field = R.drawable.class.getField("ic_add_black_24dp");
            otherIcon = field.toString();
        } catch (Exception ex) {
            Log.e("Insert Error", ex.getMessage());
        }


        otherValues.put(NewCategoryInfo.CategoryInfo.CATEGORY_NAME, "OTHER");
        otherValues.put(NewCategoryInfo.CategoryInfo.CATEGORY_DESC, "Random stuff");
        otherValues.put(NewCategoryInfo.CategoryInfo.CATEGORY_ICON, otherIcon);

        /**
         * Content resolver insert operation
         */
        db.insert(NewCategoryInfo.CategoryInfo.TABLE_NAME, null, otherValues);

    }

} //end class EyeRSDatabaseHelper
