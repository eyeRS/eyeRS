package com.github.eyers.activities;

/**
 * The class holds data for the database table and column names belonging to the Register Activity
 * Created by Nathan Shava on 05-Oct-17.
 */

public class UserRegInfo {

    public static class RegInfo {

        /**
         * Entity name
         */
        public static final String TABLE_NAME = "reg_info";
        /**
         * Attributes
         */
        public static final String REG_ID = "_id";
        public static final String USER_NAME = "user_name";
        public static final String USER_EMAIL = "user_email";
        public static final String USER_PIN = "user_pin";
        public static final String SECURITY_QUESTION = "security_question";
        public static final String SECURITY_RESPONSE = "security_response";

    }
}
