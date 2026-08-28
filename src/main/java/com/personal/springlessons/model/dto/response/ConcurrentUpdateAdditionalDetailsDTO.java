package com.personal.springlessons.model.dto.response;

import java.util.Objects;

import org.jspecify.annotations.Nullable;

public class ConcurrentUpdateAdditionalDetailsDTO {

  private @Nullable String id;
  private @Nullable String version;

  public ConcurrentUpdateAdditionalDetailsDTO() {
    // no-args constructor for Jackson deserialization
  }

  public @Nullable String getId() {
    return this.id;
  }

  public void setId(@Nullable String id) {
    this.id = id;
  }

  public @Nullable String getVersion() {
    return this.version;
  }

  public void setVersion(@Nullable String version) {
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
