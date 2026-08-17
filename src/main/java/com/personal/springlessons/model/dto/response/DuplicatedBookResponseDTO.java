package com.personal.springlessons.model.dto.response;

import java.util.Objects;

public class DuplicatedBookResponseDTO extends BaseErrorResponseDTO {

  private DuplicatedBookAdditionalDetailsDTO additionalData;

  public DuplicatedBookResponseDTO() {
    // no-args constructor for Jackson deserialization
  }

  public DuplicatedBookAdditionalDetailsDTO getAdditionalData() {
    return this.additionalData;
  }

  public void setAdditionalData(DuplicatedBookAdditionalDetailsDTO additionalData) {
    this.additionalData = additionalData;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof DuplicatedBookResponseDTO that))
      return false;
    return Objects.equals(this.additionalData, that.additionalData);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.additionalData);
  }

  @Override
  public String toString() {
    return "DuplicatedBookResponseDTO{" + super.toString() + ", additionalData="
        + this.additionalData + '}';
  }
}
