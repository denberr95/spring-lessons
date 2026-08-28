package com.personal.springlessons.model.dto.response;

import java.util.List;
import java.util.Objects;

import org.jspecify.annotations.Nullable;

public class InvalidArgumentTypeAdditionalDetailsDTO {

  private @Nullable String field;
  private @Nullable String value;
  private @Nullable List<String> pickList;

  public InvalidArgumentTypeAdditionalDetailsDTO() {
    // no-args constructor for Jackson deserialization
  }

  public @Nullable String getField() {
    return this.field;
  }

  public void setField(@Nullable String field) {
    this.field = field;
  }

  public @Nullable String getValue() {
    return this.value;
  }

  public void setValue(@Nullable String value) {
    this.value = value;
  }

  public @Nullable List<String> getPickList() {
    return this.pickList;
  }

  public void setPickList(@Nullable List<String> pickList) {
    this.pickList = pickList;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof InvalidArgumentTypeAdditionalDetailsDTO that))
      return false;
    return Objects.equals(this.field, that.field) && Objects.equals(this.value, that.value)
        && Objects.equals(this.pickList, that.pickList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.field, this.value, this.pickList);
  }

  @Override
  public String toString() {
    return "InvalidArgumentTypeAdditionalDetailsDTO{field='" + this.field + "', value='"
        + this.value + "', pickList=" + this.pickList + '}';
  }
}
