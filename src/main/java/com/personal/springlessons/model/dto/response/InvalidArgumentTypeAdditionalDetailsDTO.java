package com.personal.springlessons.model.dto.response;

import java.util.List;
import java.util.Objects;

public class InvalidArgumentTypeAdditionalDetailsDTO {

  private String field;
  private String value;
  private List<String> pickList;

  public InvalidArgumentTypeAdditionalDetailsDTO(String field, String value,
      List<String> pickList) {
    this.field = field;
    this.value = value;
    this.pickList = pickList;
  }

  public InvalidArgumentTypeAdditionalDetailsDTO() {}

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
  public int hashCode() {
    return Objects.hash(this.field, this.value, this.pickList);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (this.getClass() != obj.getClass())
      return false;
    InvalidArgumentTypeAdditionalDetailsDTO other = (InvalidArgumentTypeAdditionalDetailsDTO) obj;
    return Objects.equals(this.field, other.field) && Objects.equals(this.value, other.value)
        && Objects.equals(this.pickList, other.pickList);
  }

  @Override
  public String toString() {
    return "InvalidArgumentTypeAdditionalDetailsDTO [field=" + this.field + ", value=" + this.value
        + ", pickList=" + this.pickList + "]";
  }
}
