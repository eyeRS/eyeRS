package com.github.eyers.info;

/**
 * The class holds data about the database table and column names belonging to the User Registration
 * process in the database. Created 05-Sep-17.
 *
 * @author Nathan Shava
 */
public class NewRegInfo {

    public static abstract class UserRegistrationInfo {
        /**
         * Entity name
         */
        public static final String TABLE_NAME = "user_registration";
        /**
         * Attributes
         */
        public static final String REG_ID = "_id";
        public static final String USER_NAME = "user_name";
        public static final String EMAIL_ADD = "user_email";
        public static final String USER_PIN = "user_pin";
        public static final String SECURITY_QUESTION = "security_question";
        public static final String SECURITY_RESPONSE = "security_response";
    }
}
