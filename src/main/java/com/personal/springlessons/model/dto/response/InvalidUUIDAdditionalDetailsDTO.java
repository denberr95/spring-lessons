package com.personal.springlessons.model.dto.response;

import java.util.Objects;

public class InvalidUUIDAdditionalDetailsDTO {

  private String invalidId;

  public InvalidUUIDAdditionalDetailsDTO() {
    // no-args constructor for Jackson deserialization
  }

  public String getInvalidId() {
    return this.invalidId;
  }

  public void setInvalidId(String invalidId) {
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
