package com.personal.springlessons.model.dto.response;

import java.util.Objects;

import org.jspecify.annotations.Nullable;

public class InvalidUUIDAdditionalDetailsDTO {

  private @Nullable String invalidId;

  public InvalidUUIDAdditionalDetailsDTO() {
    // no-args constructor for Jackson deserialization
  }

  public @Nullable String getInvalidId() {
    return this.invalidId;
  }

  public void setInvalidId(@Nullable String invalidId) {
    this.invalidId = invalidId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof InvalidUUIDAdditionalDetailsDTO that))
      return false;
    return Objects.equals(this.invalidId, that.invalidId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.invalidId);
  }

  @Override
  public String toString() {
    return "InvalidUUIDAdditionalDetailsDTO{invalidId='" + this.invalidId + "'}";
  }
}
