package com.personal.springlessons.exception;

public class ConcurrentUpdateException extends RuntimeException {

  private static final long serialVersionUID = 1L;
  private final String id;
  private final String version;

  public ConcurrentUpdateException(String id, String version) {
    super(String.format("Resource id %s with version %s was updated by another transaction", id,
        version));
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
