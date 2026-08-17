package com.personal.springlessons.model.dto.response;

import java.util.Objects;

public class ValidationRequestAdditionalDetailsDTO {

  private String field;
  private String message;
  private String value;

  public ValidationRequestAdditionalDetailsDTO() {
    // no-args constructor for Jackson deserialization
  }

  public String getField() {
    return this.field;
  }

  public void setField(String field) {
    this.field = field;
  }

  public String getMessage() {
    return this.message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getValue() {
    return this.value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof ValidationRequestAdditionalDetailsDTO that))
      return false;
    return Objects.equals(this.field, that.field) && Objects.equals(this.message, that.message)
        && Objects.equals(this.value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.field, this.message, this.value);
  }

  @Override
  public String toString() {
    return "ValidationRequestAdditionalDetailsDTO{field='" + this.field + "', message='"
        + this.message + "', value='" + this.value + "'}";
  }
}
