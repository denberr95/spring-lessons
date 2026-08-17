package com.personal.springlessons.model.dto.response;

import java.util.Objects;

public class NotReadableBodyRequestAdditionalDetailsDTO {

  private String exception;

  public NotReadableBodyRequestAdditionalDetailsDTO() {
    // no-args constructor for Jackson deserialization
  }

  public String getException() {
    return this.exception;
  }

  public void setException(String exception) {
    this.exception = exception;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof NotReadableBodyRequestAdditionalDetailsDTO that))
      return false;
    return Objects.equals(this.exception, that.exception);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.exception);
  }

  @Override
  public String toString() {
    return "NotReadableBodyRequestAdditionalDetailsDTO{exception='" + this.exception + "'}";
  }
}
