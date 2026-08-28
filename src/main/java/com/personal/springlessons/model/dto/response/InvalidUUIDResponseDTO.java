package com.personal.springlessons.model.dto.response;

import java.util.Objects;

public class InvalidUUIDResponseDTO extends BaseErrorResponseDTO {

  private InvalidUUIDAdditionalDetailsDTO additionalData;

  public InvalidUUIDResponseDTO() {
    // no-args constructor for Jackson deserialization
  }

  public InvalidUUIDAdditionalDetailsDTO getAdditionalData() {
    return this.additionalData;
  }

  public void setAdditionalData(InvalidUUIDAdditionalDetailsDTO additionalData) {
    this.additionalData = additionalData;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof InvalidUUIDResponseDTO that))
      return false;
    if (!super.equals(o))
      return false;
    return Objects.equals(this.additionalData, that.additionalData);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), this.additionalData);
  }

  @Override
  public String toString() {
    return "InvalidUUIDResponseDTO{" + super.toString() + ", additionalData=" + this.additionalData
        + '}';
  }
}
