package com.personal.springlessons.model.dto.response;

import java.util.Objects;

public class ConcurrentUpdateAdditionalDetailsDTO {

  private String id;
  private String version;

  public ConcurrentUpdateAdditionalDetailsDTO() {
    // no-args constructor for Jackson deserialization
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getVersion() {
    return this.version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof ConcurrentUpdateAdditionalDetailsDTO that))
      return false;
    return Objects.equals(this.id, that.id) && Objects.equals(this.version, that.version);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.version);
  }

  @Override
  public String toString() {
    return "ConcurrentUpdateAdditionalDetailsDTO{id='" + this.id + "', version='" + this.version
        + "'}";
  }
}
