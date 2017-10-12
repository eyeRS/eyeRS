package com.github.eyers.activities;

/**
 * The class holds data about the database table and column names belonging to the User Profile Settings
 * Created 12-Oct-17.
 *
 * @author Nathan Shava
 */
public class UserProfileInfo {

    public static abstract class ProfileInfo {
        /**
         * Entity name
         */
        public static final String TABLE_NAME = "user_reg";
        /**
         * Attributes
         */
        public static final String PROFILE_ID = "_id";
        public static final String USER_NAME = "user_name";
        public static final String USER_AVATAR = "user_avatar";

    }
}
