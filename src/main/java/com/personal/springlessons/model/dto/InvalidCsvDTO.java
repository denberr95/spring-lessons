package com.personal.springlessons.model.dto;

import java.util.List;
import java.util.Objects;

import org.jspecify.annotations.Nullable;

public class InvalidCsvDTO {

  private @Nullable Integer row;
  private @Nullable List<CsvRowValidationDTO> validations;

  public InvalidCsvDTO() {
    // no-args constructor for MapStruct
  }

  public @Nullable Integer getRow() {
    return this.row;
  }

  public void setRow(@Nullable Integer row) {
    this.row = row;
  }

  public @Nullable List<CsvRowValidationDTO> getValidations() {
    return this.validations;
  }

  public void setValidations(@Nullable List<CsvRowValidationDTO> validations) {
    this.validations = validations;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof InvalidCsvDTO that))
      return false;
    return Objects.equals(this.row, that.row) && Objects.equals(this.validations, that.validations);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.row, this.validations);
  }

  @Override
  public String toString() {
    return "InvalidCsvDTO{row=" + this.row + ", validations=" + this.validations + '}';
  }
}
