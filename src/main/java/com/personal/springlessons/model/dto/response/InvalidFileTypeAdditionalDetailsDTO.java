package com.personal.springlessons.model.dto.response;

import java.util.List;
import java.util.Objects;

import org.jspecify.annotations.Nullable;

public class InvalidFileTypeAdditionalDetailsDTO {

  private @Nullable String fileName;
  private @Nullable List<String> validFileTypes;

  public InvalidFileTypeAdditionalDetailsDTO() {
    // no-args constructor for Jackson deserialization
  }

  public @Nullable String getFileName() {
    return this.fileName;
  }

  public void setFileName(@Nullable String fileName) {
    this.fileName = fileName;
  }

  public @Nullable List<String> getValidFileTypes() {
    return this.validFileTypes;
  }

  public void setValidFileTypes(@Nullable List<String> validFileTypes) {
    this.validFileTypes = validFileTypes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof InvalidFileTypeAdditionalDetailsDTO that))
      return false;
    return Objects.equals(this.fileName, that.fileName)
        && Objects.equals(this.validFileTypes, that.validFileTypes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.fileName, this.validFileTypes);
  }

  @Override
  public String toString() {
    return "InvalidFileTypeAdditionalDetailsDTO{fileName='" + this.fileName + "', validFileTypes="
        + this.validFileTypes + '}';
  }
}
