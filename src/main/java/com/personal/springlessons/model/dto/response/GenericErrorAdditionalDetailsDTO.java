package com.personal.springlessons.model.dto.response;

import java.util.Objects;

public class GenericErrorAdditionalDetailsDTO {

  private String exceptionName;
  private String exceptionMessage;

  public GenericErrorAdditionalDetailsDTO() {
    // no-args constructor for Jackson deserialization
  }

  public String getExceptionName() {
    return this.exceptionName;
  }

  public void setExceptionName(String exceptionName) {
    this.exceptionName = exceptionName;
  }

  public String getExceptionMessage() {
    return this.exceptionMessage;
  }

  public void setExceptionMessage(String exceptionMessage) {
    this.exceptionMessage = exceptionMessage;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof GenericErrorAdditionalDetailsDTO that))
      return false;
    return Objects.equals(this.exceptionName, that.exceptionName)
        && Objects.equals(this.exceptionMessage, that.exceptionMessage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.exceptionName, this.exceptionMessage);
  }

  @Override
  public String toString() {
    return "GenericErrorAdditionalDetailsDTO{exceptionName='" + this.exceptionName
        + "', exceptionMessage='" + this.exceptionMessage + "'}";
  }
}
