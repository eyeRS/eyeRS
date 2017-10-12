package com.github.eyers.info;

/**
 * Created on 04-Sep-17.
 * The class holds data for the database table and column names belonging to a NewCategoryActivity
 *
 * @author Nathan Shava
 */

public class NewCategoryInfo {

    public static class CategoryInfo {

        /**
         * Entity name
         */
        public static final String TABLE_NAME = "category_info";
        /**
         * Attributes
         */
        public static final String CATEGORY_ID = "_id";
        public static final String CATEGORY_NAME = "category_name";
        public static final String CATEGORY_DESC = "category_desc";
        public static final String CATEGORY_ICON = "category_icon";

    }

}
