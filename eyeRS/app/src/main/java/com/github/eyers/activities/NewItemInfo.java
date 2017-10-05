package com.github.eyers.activities;

/**
 * Created by Nathan Shava on 04-Sep-17.
 * The class holds data about the database table and column names belonging to an Item in the database
 */
public class NewItemInfo {

    /**
     * This class holds details of the new item to be inserted into the db.
     */
    public static class ItemInfo {

        /**
         * Table name.
         */
        public static final String TABLE_NAME = "item_info";
        //Column names
        public static final String ITEM_ID = "_id";
        public static final String ITEM_NAME = "item_name";
        public static final String ITEM_DESC = "item_desc";

    }
}
