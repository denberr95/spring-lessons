package com.personal.springlessons.model.dto;

import java.util.Arrays;
import java.util.Objects;

public class DownloadFileDTO {

  private String fileName;
  private byte[] content;

  public DownloadFileDTO(String fileName, byte[] content) {
    this.fileName = fileName;
    this.content = content;
  }

  public DownloadFileDTO() {}

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
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Arrays.hashCode(this.content);
    result = prime * result + Objects.hash(this.fileName);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (this.getClass() != obj.getClass())
      return false;
    DownloadFileDTO other = (DownloadFileDTO) obj;
    return Objects.equals(this.fileName, other.fileName)
        && Arrays.equals(this.content, other.content);
  }

  @Override
  public String toString() {
    return "DownloadFileDTO [fileName=" + this.fileName + ", content="
        + Arrays.toString(this.content) + "]";
  }
}
