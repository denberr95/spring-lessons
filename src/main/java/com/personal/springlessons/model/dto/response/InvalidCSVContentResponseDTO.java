package com.personal.springlessons.model.dto.response;

import java.util.Objects;

public class InvalidCSVContentResponseDTO extends BaseErrorResponseDTO {

  private Integer totalRows;
  private InvalidCSVContentAdditionalDetailsDTO additionalData;

  public InvalidCSVContentResponseDTO() {
    // no-args constructor for Jackson deserialization
  }

  public Integer getTotalRows() {
    return this.totalRows;
  }

  public void setTotalRows(Integer totalRows) {
    this.totalRows = totalRows;
  }

  public InvalidCSVContentAdditionalDetailsDTO getAdditionalData() {
    return this.additionalData;
  }

  public void setAdditionalData(InvalidCSVContentAdditionalDetailsDTO additionalData) {
    this.additionalData = additionalData;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof InvalidCSVContentResponseDTO that))
      return false;
    return Objects.equals(this.totalRows, that.totalRows)
        && Objects.equals(this.additionalData, that.additionalData);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.totalRows, this.additionalData);
  }

  @Override
  public String toString() {
    return "InvalidCSVContentResponseDTO{" + super.toString() + ", totalRows=" + this.totalRows
        + ", additionalData=" + this.additionalData + '}';
  }
}
