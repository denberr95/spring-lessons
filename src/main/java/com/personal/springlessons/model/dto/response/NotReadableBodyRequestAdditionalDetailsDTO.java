package com.personal.springlessons.model.dto.response;

import java.util.Objects;

public class NotReadableBodyRequestAdditionalDetailsDTO {

  private String exception;

  public NotReadableBodyRequestAdditionalDetailsDTO(String exception) {
    this.exception = exception;
  }

  public NotReadableBodyRequestAdditionalDetailsDTO() {}

  public String getException() {
    return this.exception;
  }

  public void setException(String exception) {
    this.exception = exception;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.exception);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (this.getClass() != obj.getClass())
      return false;
    NotReadableBodyRequestAdditionalDetailsDTO other =
        (NotReadableBodyRequestAdditionalDetailsDTO) obj;
    return Objects.equals(this.exception, other.exception);
  }

  @Override
  public String toString() {
    return "NotReadableBodyRequestAdditionalDetailsDTO [exception=" + this.exception + "]";
  }
}
