package com.personal.springlessons.model.dto.response;

import java.util.List;
import java.util.Objects;

import com.personal.springlessons.model.dto.InvalidCsvDTO;

import org.jspecify.annotations.Nullable;

public class InvalidCSVContentAdditionalDetailsDTO {

  private @Nullable List<InvalidCsvDTO> rows;

  public InvalidCSVContentAdditionalDetailsDTO() {
    // no-args constructor for Jackson deserialization
  }

  public @Nullable List<InvalidCsvDTO> getRows() {
    return this.rows;
  }

  public void setRows(@Nullable List<InvalidCsvDTO> rows) {
    this.rows = rows;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof InvalidCSVContentAdditionalDetailsDTO that))
      return false;
    return Objects.equals(this.rows, that.rows);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.rows);
  }

  @Override
  public String toString() {
    return "InvalidCSVContentAdditionalDetailsDTO{rows=" + this.rows + '}';
  }
}
