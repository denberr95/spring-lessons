package com.personal.springlessons.model.dto.response;

import java.util.List;
import java.util.Objects;

public class InvalidArgumentTypeAdditionalDetailsDTO {

  private String field;
  private String value;
  private List<String> pickList;

  public InvalidArgumentTypeAdditionalDetailsDTO() {
    // no-args constructor for Jackson deserialization
  }

  public String getField() {
    return this.field;
  }

  public void setField(String field) {
    this.field = field;
  }

  public String getValue() {
    return this.value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public List<String> getPickList() {
    return this.pickList;
  }

  public void setPickList(List<String> pickList) {
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
