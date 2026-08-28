package com.personal.springlessons.model.dto.response;

import java.util.Objects;

public class InvalidArgumentTypeResponseDTO extends BaseErrorResponseDTO {

  private InvalidArgumentTypeAdditionalDetailsDTO additionalData;

  public InvalidArgumentTypeResponseDTO() {
    // no-args constructor for Jackson deserialization
  }

  public InvalidArgumentTypeAdditionalDetailsDTO getAdditionalData() {
    return this.additionalData;
  }

  public void setAdditionalData(InvalidArgumentTypeAdditionalDetailsDTO additionalData) {
    this.additionalData = additionalData;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof InvalidArgumentTypeResponseDTO that))
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
    return "InvalidArgumentTypeResponseDTO{" + super.toString() + ", additionalData="
        + this.additionalData + '}';
  }
}
