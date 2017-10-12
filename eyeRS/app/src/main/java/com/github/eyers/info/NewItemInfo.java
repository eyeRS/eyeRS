package com.github.eyers.info;

/**
 * Created on 04-Sep-17
 * The class holds data about the database table and column names belonging to the NewItemActivity
 *
 * @author Nathan Shava
 */

public class NewItemInfo {

    public static class ItemInfo {

        /**
         * Entity name.
         */
        public static final String TABLE_NAME = "item_info";
        /**
         * Attributes.
         */
        public static final String ITEM_ID = "_id";
        public static final String CATEGORY_NAME = "category_name";
        public static final String ITEM_NAME = "item_name";
        public static final String ITEM_DESC = "item_desc";
        public static final String ITEM_IMAGE = "item_image";

    }

}
