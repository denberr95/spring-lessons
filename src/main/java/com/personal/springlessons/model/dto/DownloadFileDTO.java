package com.personal.springlessons.model.dto;

import java.util.Arrays;
import java.util.Objects;

public class DownloadFileDTO {

  private String fileName;
  private byte[] content;

  public DownloadFileDTO() {
    // no-args constructor for MapStruct
  }

  public String getFileName() {
    return this.fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public byte[] getContent() {
    return this.content;
  }

  public void setContent(byte[] content) {
    this.content = content;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof DownloadFileDTO that))
      return false;
    return Objects.equals(this.fileName, that.fileName)
        && Arrays.equals(this.content, that.content);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.fileName, Arrays.hashCode(this.content));
  }

  @Override
  public String toString() {
    return "DownloadFileDTO{" + "fileName='" + this.fileName + '\'' + ", content="
        + Arrays.toString(this.content) + '}';
  }
}
