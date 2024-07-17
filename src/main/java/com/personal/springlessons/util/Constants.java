package com.personal.springlessons.util;

public class Constants {

    private Constants() {
        throw new UnsupportedOperationException(
                "This is a utility class and cannot be instantiated");
    }

    // Database schemas
    public static final String DB_SCHEMA_SPRING_APP = "SPRING_APP";

    // Error Messages
    public static final String ERROR_MSG_LEN_VALIDATION =
            "Must be between {min} and {max} characters long";
    public static final String ERROR_MSG_MIN_VALUE = "Must be greater than or equal to {value}";
    public static final String ERROR_MSG_NOT_BLANK = "Cannot be null or empty";

    // Lenght
    public static final int LEN_1 = 1;
    public static final int LEN_100 = 100;
}
