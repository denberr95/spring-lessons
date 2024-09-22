package com.personal.springlessons.util;

public final class Constants {

    private Constants() {
        throw new UnsupportedOperationException(
                "This is a utility class and cannot be instantiated");
    }

    // Span Key Tag
    public static final String TOTAL_ORDERS = "total.orders";
    public static final String TOTAL_ITEMS = "total.items";
    public static final String OPERATION_TYPE = "operation.type";
    public static final String BARCODE = "barcode";
    public static final String NUMBER_ITEMS_TO_UPLOAD = "number.items.to.upload";
    public static final String NUMBER_ITEMS_TO_DELETE = "number.items.to.delete";
    public static final String ID_ORDER_ITEMS = "id.order.items";
    public static final String COLLECTED_ITEMS = "collected.items";

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
    public static final String S_VAL_1 = "1";
    public static final String S_VAL_3 = "3";

    // Other
    public static final char SEMICOLON = ';';
    public static final char UNDERSCORE = '_';
    public static final char DOT = '.';
    public static final char APOSTROPHE = '\'';
    public static final String CSV = "csv";

    // File System Extension
    public static final String CSV_EXT = DOT + CSV;

    // Date Time Format
    public static final String DATE_TIME_FORMAT_1 = "yyyyMMddHHmmss";
}
