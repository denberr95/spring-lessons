package com.personal.springlessons.model.dto.response;

import java.util.Objects;

public class MissingHttpRequestHeaderResponseDTO extends BaseErrorResponseDTO {

  private MissingHttpRequestHeaderAdditionalDetailsDTO additionalData;

  public MissingHttpRequestHeaderResponseDTO() {
    // no-args constructor for Jackson deserialization
  }

  public MissingHttpRequestHeaderAdditionalDetailsDTO getAdditionalData() {
    return this.additionalData;
  }

  public void setAdditionalData(MissingHttpRequestHeaderAdditionalDetailsDTO additionalData) {
    this.additionalData = additionalData;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof MissingHttpRequestHeaderResponseDTO that))
      return false;
    return Objects.equals(this.additionalData, that.additionalData);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.additionalData);
  }

  @Override
  public String toString() {
    return "MissingHttpRequestHeaderResponseDTO{" + super.toString() + ", additionalData="
        + this.additionalData + '}';
  }
}
