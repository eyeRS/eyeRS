package com.github.eyers.activities;

import java.sql.Blob;

/**
 * Created by Nathan Shava on 04-Sep-17.
 * The class holds data for the database table and column names belonging to a Category in the database
 */

public class NewCategoryInfo {

    //This class will hold details of the new category to be inserted into the db
    public static class CategoryInfo {

        //Table name
        public static final String TABLE_NAME = "category_info";
        //Column names
        public static final String CATEGORY_ID = "_id";
        public static final String CATEGORY_NAME = "category_name";
        public static final String CATEGORY_DESC = "category_desc";
    }

}
