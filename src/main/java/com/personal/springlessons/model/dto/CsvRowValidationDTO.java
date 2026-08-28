package com.personal.springlessons.model.dto;

import java.util.Objects;

import org.jspecify.annotations.Nullable;

public class CsvRowValidationDTO {

  private @Nullable String field;
  private @Nullable Object value;
  private @Nullable String message;

  public CsvRowValidationDTO() {
    // no-args constructor for MapStruct
  }

  public @Nullable String getField() {
    return this.field;
  }

  public void setField(@Nullable String field) {
    this.field = field;
  }

  public @Nullable Object getValue() {
    return this.value;
  }

  public void setValue(@Nullable Object value) {
    this.value = value;
  }

  public @Nullable String getMessage() {
    return this.message;
  }

  public void setMessage(@Nullable String message) {
    this.message = message;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof CsvRowValidationDTO that))
      return false;
    return Objects.equals(this.field, that.field) && Objects.equals(this.value, that.value)
        && Objects.equals(this.message, that.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.field, this.value, this.message);
  }

  @Override
  public String toString() {
    return "CsvRowValidationDTO{" + "field='" + this.field + '\'' + ", value=" + this.value
        + ", message='" + this.message + '\'' + '}';
  }
}
