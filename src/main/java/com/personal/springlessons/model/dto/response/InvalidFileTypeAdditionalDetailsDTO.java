package com.personal.springlessons.model.dto.response;

import java.util.List;
import java.util.Objects;

public class InvalidFileTypeAdditionalDetailsDTO {

  private String fileName;
  private List<String> validFileTypes;

  public InvalidFileTypeAdditionalDetailsDTO() {
    // no-args constructor for Jackson deserialization
  }

  public String getFileName() {
    return this.fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public List<String> getValidFileTypes() {
    return this.validFileTypes;
  }

  public void setValidFileTypes(List<String> validFileTypes) {
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
