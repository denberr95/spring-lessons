package com.personal.springlessons.model.dto.response;

import java.util.Objects;

public class NotReadableBodyRequestResponseDTO extends BaseErrorResponseDTO {

  private NotReadableBodyRequestAdditionalDetailsDTO additionalData;

  public NotReadableBodyRequestResponseDTO() {
    // no-args constructor for Jackson deserialization
  }

  public NotReadableBodyRequestAdditionalDetailsDTO getAdditionalData() {
    return this.additionalData;
  }

  public void setAdditionalData(NotReadableBodyRequestAdditionalDetailsDTO additionalData) {
    this.additionalData = additionalData;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof NotReadableBodyRequestResponseDTO that))
      return false;
    return Objects.equals(this.additionalData, that.additionalData);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.additionalData);
  }

  @Override
  public String toString() {
    return "NotReadableBodyRequestResponseDTO{" + super.toString() + ", additionalData="
        + this.additionalData + '}';
  }
}
