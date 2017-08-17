package com.github.eyers.activities;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EyeRSDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "eyers"; //the name of the database
    private static final int DB_VERSION = 1; //the version of the database

    EyeRSDatabaseHelper(Context context){
        /*
            We're calling the constructor of the SQLiteOpenHelper superclass,
            and passing it the database name and version
         */

        //the null parameter is an advanced feature relating to cursors
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        updateMyDatabase(db, oldVersion, newVersion);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion <= 1){

            db.execSQL("CREATE TABLE BOOKS ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "IMAGE_RESOURCE_ID INTEGER, "
					+ "NAME TEXT, "
                    + "DESCRIPTION TEXT);");
                    
			db.execSQL("CREATE TABLE CLOTHES ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "IMAGE_RESOURCE_ID INTEGER, "
					+ "NAME TEXT, "
                    + "DESCRIPTION TEXT);");
            
			db.execSQL("CREATE TABLE ACCESSORIES ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "IMAGE_RESOURCE_ID INTEGER, "
					+ "NAME TEXT, "
                    + "DESCRIPTION TEXT);");
					
			db.execSQL("CREATE TABLE GAMES ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "IMAGE_RESOURCE_ID INTEGER, "
					+ "NAME TEXT, "
                    + "DESCRIPTION TEXT);");
					
			db.execSQL("CREATE TABLE OTHER ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "IMAGE_RESOURCE_ID INTEGER, "
					+ "NAME TEXT, "
                    + "DESCRIPTION TEXT);");
        }
        if (oldVersion >= 2){
            //Code to be executed when eyers db is updated to a higher version
            
        }
    }

    private void insertBook(SQLiteDatabase db, String name, String description, int resourceId) {
        ContentValues bookValues = new ContentValues();
        bookValues.put("IMAGE_RESOURCE_ID", resourceId);
		bookValues.put("NAME", name);
        bookValues.put("DESCRIPTION", description);
        db.insert("BOOKS", null, bookValues);
    }
	
	private void insertClothes(SQLiteDatabase db, String name, String description, int resourceId) {
        ContentValues clothesValues = new ContentValues();
        clothesValues.put("IMAGE_RESOURCE_ID", resourceId);
		clothesValues.put("NAME", name);
        clothesValues.put("DESCRIPTION", description);
        db.insert("CLOTHES", null, clothesValues);
    }
	
	private void insertAccessories(SQLiteDatabase db, String name, String description, int resourceId) {
        ContentValues accessoriesValues = new ContentValues();
        accessoriesValues.put("IMAGE_RESOURCE_ID", resourceId);
		accessoriesValues.put("NAME", name);
        accessoriesValues.put("DESCRIPTION", description);
        db.insert("ACCESSORIES", null, accessoriesValues);
    }
	
	private void insertGames(SQLiteDatabase db, String name, String description, int resourceId) {
        ContentValues gamesValues = new ContentValues();
        gamesValues.put("IMAGE_RESOURCE_ID", resourceId);
		gamesValues.put("NAME", name);
        gamesValues.put("DESCRIPTION", description);
        db.insert("GAMES", null, gamesValues);
    }
	
	private void insertOther(SQLiteDatabase db, String name, String description, int resourceId) {

        ContentValues otherValues = new ContentValues();
        otherValues.put("IMAGE_RESOURCE_ID", resourceId);
		otherValues.put("NAME", name);
        otherValues.put("DESCRIPTION", description);
        db.insert("OTHER", null, otherValues);
    }
}
