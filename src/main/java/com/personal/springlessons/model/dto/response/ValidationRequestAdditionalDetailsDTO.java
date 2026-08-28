package com.personal.springlessons.model.dto.response;

import java.util.Objects;

import org.jspecify.annotations.Nullable;

public class ValidationRequestAdditionalDetailsDTO {

  private @Nullable String field;
  private @Nullable String message;
  private @Nullable String value;

  public ValidationRequestAdditionalDetailsDTO() {
    // no-args constructor for Jackson deserialization
  }

  public @Nullable String getField() {
    return this.field;
  }

  public void setField(@Nullable String field) {
    this.field = field;
  }

  public @Nullable String getMessage() {
    return this.message;
  }

  public void setMessage(@Nullable String message) {
    this.message = message;
  }

  public @Nullable String getValue() {
    return this.value;
  }

  public void setValue(@Nullable String value) {
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
