package com.personal.springlessons.util;

public final class Constants {

  private Constants() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  // Span Key Tag
  public static final String SPAN_KEY_FILE_NAME = "file.name";
  public static final String SPAN_KEY_TOTAL_ORDERS = "total.orders";
  public static final String SPAN_KEY_TOTAL_BOOKS = "total.books";
  public static final String SPAN_KEY_TOTAL_ROWS = "total.rows";
  public static final String SPAN_KEY_TOTAL_INVALID_ROWS = "total.invalid.rows";
  public static final String SPAN_KEY_BOOKS_NAME = "books.name";
  public static final String SPAN_KEY_OPERATION_TYPE = "operation.type";
  public static final String SPAN_KEY_BARCODE = "barcode";
  public static final String SPAN_KEY_NUMBER_ITEMS_TO_UPLOAD = "number.items.to.upload";
  public static final String SPAN_KEY_NUMBER_ITEMS_TO_DELETE = "number.items.to.delete";
  public static final String SPAN_KEY_ID_ORDER_ITEMS = "id.order.items";
  public static final String SPAN_KEY_ID_ITEMS = "id.items";
  public static final String SPAN_KEY_ID_BOOKS = "id.books";
  public static final String SPAN_KEY_EMAIL_TO = "email.to";
  public static final String SPAN_KEY_PAGE_NUMBER = "page.number";
  public static final String SPAN_KEY_PAGE_SIZE = "page.size";

  // Database schemas
  public static final String DB_SCHEMA_SPRING_APP = "SPRING_APP";
  public static final String DB_SCHEMA_HISTORY = "HISTORY";

  // Kafka Topics
  public static final String TOPIC_ITEMS = "topic-items";

  // Error Messages
  public static final String ERROR_MSG_LEN_VALIDATION =
      "Must be between {min} and {max} characters long";
  public static final String ERROR_MSG_LIST_SIZE_VALIDATION =
      "Must be between {min} and {max} list size";
  public static final String ERROR_MSG_MIN_VALUE = "Must be greater than or equal to {value}";
  public static final String ERROR_MSG_NOT_BLANK = "Cannot be null or empty";
  public static final String ERROR_MSG_POSITIVE_VALUE = "Must be a positive value";
  public static final String ERROR_MSG_MAX_VALUE = "Must be less than or equal to {value}";

  // Int Values
  public static final int I_VAL_1 = 1;
  public static final int I_VAL_2 = 2;
  public static final int I_VAL_6 = 6;
  public static final int I_VAL_10 = 10;
  public static final int I_VAL_45 = 45;
  public static final int I_VAL_50 = 50;
  public static final int I_VAL_100 = 100;
  public static final int I_VAL_255 = 255;

  // String Values
  public static final String S_VAL_0_01 = "0.01";
  public static final String S_VAL_1 = "1";
  public static final String S_VAL_3 = "3";
  public static final String S_VAL_9999_99 = "9999.99";

  // Other Values
  public static final String S_EMPTY = "";
  public static final String S_DOT = ".";
  public static final String S_DOUBLE_QUOTE = "\"";
  public static final String S_REGEX_ANY_SPACE = "\\s+";
  public static final String S_CSV = "csv";
  public static final String S_ETAG = "ETag";
  public static final String S_X_TOTAL_RECORDS = "X-Total-Records";
  public static final String S_X_FORWARDED_FOR = "X-Forwarded-For";
  public static final String S_UNKNOWN_IP_ADDRESS = "UNKNOWN_IP_ADDRESS";
  public static final String S_UNKNOWN_CLIENT_ID = "UNKNOWN_CLIENT_ID";
  public static final String S_UNKNOWN_REQUEST_URI = "UNKNOWN_REQUEST_URI";
  public static final String S_UNKNOWN_HTTP_METHOD = "UNKNOWN_HTTP_METHOD";
  public static final String S_UNKNOWN_USERNAME = "UNKNOWN_USERNAME";
  public static final String S_AZP = "azp";
  public static final String S_CLIENT_ID = "client_id";
  public static final String S_PREFERRED_USERNAME = "preferred_username";
  public static final String S_SUB = "sub";
  public static final char C_SEMICOLON = ';';
  public static final char C_UNDERSCORE = '_';
  public static final char C_APOSTROPHE = '\'';

  // File System Extension
  public static final String CSV_EXT = S_DOT + S_CSV;

  // Date Time Format
  public static final String S_DATE_TIME_FORMAT_1 = "yyyyMMddHHmmss";
  public static final String S_DATE_FORMAT_1 = "yyyy-MM-dd";

  // XML Namespace
  public static final String S_XML_NAMESPACE_PLATFORM_HISTORY =
      "http://personal.com/springlessons/history";
  public static final String S_XML_NAMESPACE_PLATFORM_FAULT =
      "http://personal.com/springlessons/fault";
  
  // QName Soap Tag
  public static final String S_SOAP_TAG_TIMESTAMP = "timestamp";
  public static final String S_SOAP_TAG_EXCEPTION = "exception";
  public static final String S_SOAP_TAG_ID = "id";
}
