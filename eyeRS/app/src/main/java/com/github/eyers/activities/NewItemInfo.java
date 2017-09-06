package com.github.eyers.activities;

import java.sql.Blob;

/**
 * Created by Nathan Shava on 04-Sep-17.
 */

public class NewItemInfo {

    //This class holds details of the new item to be inserted into the db
    public static abstract class ItemInfo {

        //Table name
        public static final String TABLE_NAME = "item_info";
        //Column names
        public static final String ITEM_NAME = "item_name";
        public static final String ITEM_DESC = "item_desc";
        public static String DATE_ADDED;
        public static Blob ITEM_ICON;

    }
}
