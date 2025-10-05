package com.personal.springlessons.exception;

public class InvalidUUIDException extends RuntimeException {

  private static final long serialVersionUID = 1L;
  private final String id;

  public InvalidUUIDException(String id) {
    super(String.format("ID '%s' malformed, is not a valid UUID", id));
    this.id = id;
  }

  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  public String getId() {
    return this.id;
  }
}
