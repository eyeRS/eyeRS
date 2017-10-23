package com.github.eyers.info;

/**
 * The class holds data about the database table and column names belonging to the User Registration
 * process in the database. Created 05-Sep-17.
 *
 * @author Nathan Shava
 */
public interface UserRegistrationInfo {

    /**
     * Entity name.
     */
    String TABLE_NAME = "user_registration";

    // Attributes
    String REG_ID = "_id";
    String USER_NAME = "user_name";
    String EMAIL_ADD = "user_email";
    String USER_PIN = "user_pin";
    String SECURITY_QUESTION = "security_question";
    String SECURITY_RESPONSE = "security_response";
}
