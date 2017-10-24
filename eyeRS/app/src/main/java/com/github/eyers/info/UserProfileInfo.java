package com.github.eyers.info;

/**
 * The class holds data about the database table and column names belonging to the User Profile Settings
 * Created 12-Oct-17.
 *
 * @author Nathan Shava
 */
public interface UserProfileInfo {

    /**
     * Entity name.
     */
    String TABLE_NAME = "user_profile";

    /**
     * Attributes
     */
    String PROFILE_ID = "_id";
    String USER_NAME = "user_name";
    String USER_AVATAR = "user_avatar";
}
