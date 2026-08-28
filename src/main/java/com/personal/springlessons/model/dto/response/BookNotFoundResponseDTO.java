package com.personal.springlessons.model.dto.response;

import java.util.Objects;

public class BookNotFoundResponseDTO extends BaseErrorResponseDTO {

  private BookNotFoundAdditionalDetailsDTO additionalData;

  public BookNotFoundResponseDTO() {
    // no-args constructor for Jackson deserialization
  }

  public BookNotFoundAdditionalDetailsDTO getAdditionalData() {
    return this.additionalData;
  }

  public void setAdditionalData(BookNotFoundAdditionalDetailsDTO additionalData) {
    this.additionalData = additionalData;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof BookNotFoundResponseDTO that))
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
    return "BookNotFoundResponseDTO{" + super.toString() + ", additionalData=" + this.additionalData
        + '}';
  }
}
