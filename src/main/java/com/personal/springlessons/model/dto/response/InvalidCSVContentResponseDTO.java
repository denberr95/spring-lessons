package com.personal.springlessons.model.dto.response;

import java.util.Objects;

import org.jspecify.annotations.Nullable;

public class InvalidCSVContentResponseDTO extends BaseErrorResponseDTO {

  private @Nullable Integer totalRows;
  private @Nullable InvalidCSVContentAdditionalDetailsDTO additionalData;

  public InvalidCSVContentResponseDTO() {
    // no-args constructor for Jackson deserialization
  }

  public @Nullable Integer getTotalRows() {
    return this.totalRows;
  }

  public void setTotalRows(@Nullable Integer totalRows) {
    this.totalRows = totalRows;
  }

  public @Nullable InvalidCSVContentAdditionalDetailsDTO getAdditionalData() {
    return this.additionalData;
  }

  public void setAdditionalData(@Nullable InvalidCSVContentAdditionalDetailsDTO additionalData) {
    this.additionalData = additionalData;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof InvalidCSVContentResponseDTO that))
      return false;
    if (!super.equals(o))
      return false;
    return Objects.equals(this.totalRows, that.totalRows)
        && Objects.equals(this.additionalData, that.additionalData);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), this.totalRows, this.additionalData);
  }

  @Override
  public String toString() {
    return "InvalidCSVContentResponseDTO{" + super.toString() + ", totalRows=" + this.totalRows
        + ", additionalData=" + this.additionalData + '}';
  }
}
