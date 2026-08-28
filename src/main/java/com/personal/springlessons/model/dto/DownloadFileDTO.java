package com.personal.springlessons.model.dto;

import java.util.Arrays;
import java.util.Objects;

import org.jspecify.annotations.Nullable;

public class DownloadFileDTO {

  private @Nullable String fileName;
  private byte @Nullable [] content;

  public DownloadFileDTO() {
    // no-args constructor for MapStruct
  }

  public @Nullable String getFileName() {
    return this.fileName;
  }

  public void setFileName(@Nullable String fileName) {
    this.fileName = fileName;
  }

  public byte @Nullable [] getContent() {
    return this.content == null ? null : this.content.clone();
  }

  public void setContent(byte @Nullable [] content) {
    this.content = content == null ? null : content.clone();
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
