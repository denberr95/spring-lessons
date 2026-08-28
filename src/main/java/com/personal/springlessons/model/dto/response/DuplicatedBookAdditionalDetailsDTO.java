package com.personal.springlessons.model.dto.response;

import java.util.Objects;

public class DuplicatedBookAdditionalDetailsDTO {

  private String originalId;

  public DuplicatedBookAdditionalDetailsDTO() {
    // no-args constructor for Jackson deserialization
  }

  public String getOriginalId() {
    return this.originalId;
  }

  public void setOriginalId(String originalId) {
    this.originalId = originalId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof DuplicatedBookAdditionalDetailsDTO that))
      return false;
    return Objects.equals(this.originalId, that.originalId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.originalId);
  }

  @Override
  public String toString() {
    return "DuplicatedBookAdditionalDetailsDTO{originalId='" + this.originalId + "'}";
  }
}
