package com.personal.springlessons.util;

public class Constants {

    private Constants() {
        throw new UnsupportedOperationException(
                "This is a utility class and cannot be instantiated");
    }

    // Database schemas
    public static final String DB_SCHEMA_SPRING_APP = "SPRING_APP";

    // Kafka Topics
    public static final String TOPIC_ITEMS = "topic-items";

    // Error Messages
    public static final String ERROR_MSG_LEN_VALIDATION =
            "Must be between {min} and {max} characters long";
    public static final String ERROR_MSG_MIN_VALUE = "Must be greater than or equal to {value}";
    public static final String ERROR_MSG_NOT_BLANK = "Cannot be null or empty";

    // Int Values
    public static final int I_VAL_1 = 1;
    public static final int I_VAL_2 = 2;
    public static final int I_VAL_6 = 6;
    public static final int I_VAL_50 = 50;
    public static final int I_VAL_100 = 100;

    // String Values
    public static final String S_VAL_3 = "3";
}
