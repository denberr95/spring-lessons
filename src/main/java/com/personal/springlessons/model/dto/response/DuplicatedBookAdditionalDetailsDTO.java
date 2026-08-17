package com.personal.springlessons.model.dto.response;

import java.util.Objects;

public class DuplicatedBookAdditionalDetailsDTO {

  private String orinalId;

  public DuplicatedBookAdditionalDetailsDTO() {
    // no-args constructor for Jackson deserialization
  }

  public String getOrinalId() {
    return this.orinalId;
  }

  public void setOrinalId(String orinalId) {
    this.orinalId = orinalId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof DuplicatedBookAdditionalDetailsDTO that))
      return false;
    return Objects.equals(this.orinalId, that.orinalId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.orinalId);
  }

  @Override
  public String toString() {
    return "DuplicatedBookAdditionalDetailsDTO{orinalId='" + this.orinalId + "'}";
  }
}
