package com.personal.springlessons.model.dto.response;

import java.util.Objects;

public class InvalidFileTypeResponseDTO extends BaseErrorResponseDTO {

  private InvalidFileTypeAdditionalDetailsDTO additionalData;

  public InvalidFileTypeResponseDTO() {
    // no-args constructor for Jackson deserialization
  }

  public InvalidFileTypeAdditionalDetailsDTO getAdditionalData() {
    return this.additionalData;
  }

  public void setAdditionalData(InvalidFileTypeAdditionalDetailsDTO additionalData) {
    this.additionalData = additionalData;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof InvalidFileTypeResponseDTO that))
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
    return "InvalidFileTypeResponseDTO{" + super.toString() + ", additionalData="
        + this.additionalData + '}';
  }
}
