package com.github.eyers.info;

/**
 * The class holds data about the database table and column names belonging to the NewItemActivity.
 * Created on 04-Sep-17
 *
 * @author Nathan Shava
 */
public interface ItemInfo {

    /**
     * Entity name.
     */
    String TABLE_NAME = "item_info";

    // Attributes
    String ITEM_ID = "_id";
    String CATEGORY_NAME = "category_name";
    String ITEM_NAME = "item_name";
    String ITEM_DESC = "item_desc";
    String ITEM_IMAGE = "item_image";
}
