package com.personal.springlessons.model.dto.response;

import java.util.Objects;

public class ConcurrentUpdateResponseDTO extends BaseErrorResponseDTO {

  private ConcurrentUpdateAdditionalDetailsDTO additionalData;

  public ConcurrentUpdateResponseDTO() {
    // no-args constructor for Jackson deserialization
  }

  public ConcurrentUpdateAdditionalDetailsDTO getAdditionalData() {
    return this.additionalData;
  }

  public void setAdditionalData(ConcurrentUpdateAdditionalDetailsDTO additionalData) {
    this.additionalData = additionalData;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof ConcurrentUpdateResponseDTO that))
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
    return "ConcurrentUpdateResponseDTO{" + super.toString() + ", additionalData="
        + this.additionalData + '}';
  }
}
