package com.personal.springlessons.exception;

public class PreconditionFailedException extends RuntimeException {

  private static final long serialVersionUID = 1L;
  private final String id;
  private final String version;

  public PreconditionFailedException(String id, String version) {
    super(String.format(
        "Precondition failed for resource id %s: provided If-Match does not match current version %s",
        id, version));
    this.id = id;
    this.version = version;
  }

  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  public String getId() {
    return this.id;
  }

  public String getVersion() {
    return this.version;
  }
}
