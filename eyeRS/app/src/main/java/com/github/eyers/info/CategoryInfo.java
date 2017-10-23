package com.github.eyers.info;

/**
 * The class holds data for the database table and column names belonging to a NewCategoryActivity.
 * Created on 04-Sep-17.
 *
 * @author Nathan Shava
 */
public interface CategoryInfo {

    /**
     * Entity name
     */
    String TABLE_NAME = "category_info";

    // Attributes
    String CATEGORY_ID = "_id";
    String CATEGORY_NAME = "category_name";
    String CATEGORY_DESC = "category_desc";
    String CATEGORY_ICON = "category_icon";
}
