package com.personal.springlessons.model.dto.response;

import java.util.List;
import java.util.Objects;

public class ValidationRequestErrorResponseDTO extends BaseErrorResponseDTO {

  private List<ValidationRequestAdditionalDetailsDTO> additionalData;

  public ValidationRequestErrorResponseDTO() {
    // no-args constructor for Jackson deserialization
  }

  public List<ValidationRequestAdditionalDetailsDTO> getAdditionalData() {
    return this.additionalData;
  }

  public void setAdditionalData(List<ValidationRequestAdditionalDetailsDTO> additionalData) {
    this.additionalData = additionalData;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof ValidationRequestErrorResponseDTO that))
      return false;
    return Objects.equals(this.additionalData, that.additionalData);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.additionalData);
  }

  @Override
  public String toString() {
    return "ValidationRequestErrorResponseDTO{" + super.toString() + ", additionalData="
        + this.additionalData + '}';
  }
}
