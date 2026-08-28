package com.personal.springlessons.model.dto.response;

import java.util.List;
import java.util.Objects;

import org.jspecify.annotations.Nullable;

public class ValidationRequestErrorResponseDTO extends BaseErrorResponseDTO {

  private @Nullable List<ValidationRequestAdditionalDetailsDTO> additionalData;

  public ValidationRequestErrorResponseDTO() {
    // no-args constructor for Jackson deserialization
  }

  public @Nullable List<ValidationRequestAdditionalDetailsDTO> getAdditionalData() {
    return this.additionalData;
  }

  public void setAdditionalData(
      @Nullable List<ValidationRequestAdditionalDetailsDTO> additionalData) {
    this.additionalData = additionalData;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof ValidationRequestErrorResponseDTO that))
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
    return "ValidationRequestErrorResponseDTO{" + super.toString() + ", additionalData="
        + this.additionalData + '}';
  }
}
