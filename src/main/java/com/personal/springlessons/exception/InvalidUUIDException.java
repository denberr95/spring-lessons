package com.personal.springlessons.exception;

import org.jspecify.annotations.Nullable;

public class InvalidUUIDException extends RuntimeException {

  private static final long serialVersionUID = 1L;
  private final @Nullable String id;

  public InvalidUUIDException(@Nullable String id) {
    super(String.format("ID '%s' malformed, is not a valid UUID", id));
    this.id = id;
  }

  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  public @Nullable String getId() {
    return this.id;
  }
}
