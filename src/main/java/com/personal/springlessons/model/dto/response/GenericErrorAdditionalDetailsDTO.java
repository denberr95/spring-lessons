package com.personal.springlessons.model.dto.response;

import java.util.Objects;

import org.jspecify.annotations.Nullable;

public class GenericErrorAdditionalDetailsDTO {

  private @Nullable String exceptionName;
  private @Nullable String exceptionMessage;

  public GenericErrorAdditionalDetailsDTO() {
    // no-args constructor for Jackson deserialization
  }

  public @Nullable String getExceptionName() {
    return this.exceptionName;
  }

  public void setExceptionName(@Nullable String exceptionName) {
    this.exceptionName = exceptionName;
  }

  public @Nullable String getExceptionMessage() {
    return this.exceptionMessage;
  }

  public void setExceptionMessage(@Nullable String exceptionMessage) {
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
