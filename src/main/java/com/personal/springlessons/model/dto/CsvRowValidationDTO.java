package com.personal.springlessons.model.dto;

import java.util.Objects;

public class CsvRowValidationDTO {

  private String field;
  private Object value;
  private String message;

  public CsvRowValidationDTO() {
    // no-args constructor for MapStruct
  }

  public String getField() {
    return this.field;
  }

  public void setField(String field) {
    this.field = field;
  }

  public Object getValue() {
    return this.value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public String getMessage() {
    return this.message;
  }

  public void setMessage(String message) {
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
