package com.personal.springlessons.model.dto.response;

import java.util.List;
import java.util.Objects;

import com.personal.springlessons.model.dto.InvalidCsvDTO;

public class InvalidCSVContentAdditionalDetailsDTO {

  private List<InvalidCsvDTO> rows;

  public InvalidCSVContentAdditionalDetailsDTO(List<InvalidCsvDTO> rows) {
    this.rows = rows;
  }

  public InvalidCSVContentAdditionalDetailsDTO() {}

  public List<InvalidCsvDTO> getRows() {
    return this.rows;
  }

  public void setRows(List<InvalidCsvDTO> rows) {
    this.rows = rows;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.rows);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (this.getClass() != obj.getClass())
      return false;
    InvalidCSVContentAdditionalDetailsDTO other = (InvalidCSVContentAdditionalDetailsDTO) obj;
    return Objects.equals(this.rows, other.rows);
  }

  @Override
  public String toString() {
    return "InvalidCSVContentAdditionalDetailsDTO [rows=" + this.rows + "]";
  }
}
