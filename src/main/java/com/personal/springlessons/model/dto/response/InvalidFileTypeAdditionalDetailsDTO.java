package com.personal.springlessons.model.dto.response;

import java.util.List;
import java.util.Objects;

public class InvalidFileTypeAdditionalDetailsDTO {

  private String fileName;
  private List<String> validFileTypes;

  public InvalidFileTypeAdditionalDetailsDTO(String fileName, List<String> validFileTypes) {
    this.fileName = fileName;
    this.validFileTypes = validFileTypes;
  }

  public InvalidFileTypeAdditionalDetailsDTO() {}

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
  public int hashCode() {
    return Objects.hash(this.fileName, this.validFileTypes);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (this.getClass() != obj.getClass())
      return false;
    InvalidFileTypeAdditionalDetailsDTO other = (InvalidFileTypeAdditionalDetailsDTO) obj;
    return Objects.equals(this.fileName, other.fileName)
        && Objects.equals(this.validFileTypes, other.validFileTypes);
  }

  @Override
  public String toString() {
    return "InvalidFileTypeAdditionalDetailsDTO [fileName=" + this.fileName + ", validFileTypes="
        + this.validFileTypes + "]";
  }
}
