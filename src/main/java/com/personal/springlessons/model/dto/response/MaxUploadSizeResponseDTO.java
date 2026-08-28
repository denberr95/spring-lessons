package com.personal.springlessons.model.dto.response;

import java.util.Objects;

public class MaxUploadSizeResponseDTO extends BaseErrorResponseDTO {

  private MaxUploadSizeAdditionalDetailsDTO additionalData;

  public MaxUploadSizeResponseDTO() {
    // no-args constructor for Jackson deserialization
  }

  public MaxUploadSizeAdditionalDetailsDTO getAdditionalData() {
    return this.additionalData;
  }

  public void setAdditionalData(MaxUploadSizeAdditionalDetailsDTO additionalData) {
    this.additionalData = additionalData;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof MaxUploadSizeResponseDTO that))
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
    return "MaxUploadSizeResponseDTO{" + super.toString() + ", additionalData="
        + this.additionalData + '}';
  }
}
