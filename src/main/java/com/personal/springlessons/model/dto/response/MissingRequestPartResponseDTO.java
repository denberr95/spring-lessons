package com.personal.springlessons.model.dto.response;

import java.util.Objects;

public class MissingRequestPartResponseDTO extends BaseErrorResponseDTO {

  private MissingRequestPartAdditionalDetailsDTO additionalData;

  public MissingRequestPartResponseDTO() {
    // no-args constructor for Jackson deserialization
  }

  public MissingRequestPartAdditionalDetailsDTO getAdditionalData() {
    return this.additionalData;
  }

  public void setAdditionalData(MissingRequestPartAdditionalDetailsDTO additionalData) {
    this.additionalData = additionalData;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof MissingRequestPartResponseDTO that))
      return false;
    return Objects.equals(this.additionalData, that.additionalData);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.additionalData);
  }

  @Override
  public String toString() {
    return "MissingRequestPartResponseDTO{" + super.toString() + ", additionalData="
        + this.additionalData + '}';
  }
}
