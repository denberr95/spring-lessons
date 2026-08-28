package com.personal.springlessons.model.dto.response;

import java.util.Objects;

public class GenericErrorResponseDTO extends BaseErrorResponseDTO {

  private GenericErrorAdditionalDetailsDTO additionalData;

  public GenericErrorResponseDTO() {
    // no-args constructor for Jackson deserialization
  }

  public GenericErrorAdditionalDetailsDTO getAdditionalData() {
    return this.additionalData;
  }

  public void setAdditionalData(GenericErrorAdditionalDetailsDTO additionalData) {
    this.additionalData = additionalData;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof GenericErrorResponseDTO that))
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
    return "GenericErrorResponseDTO{" + super.toString() + ", additionalData=" + this.additionalData
        + '}';
  }
}
