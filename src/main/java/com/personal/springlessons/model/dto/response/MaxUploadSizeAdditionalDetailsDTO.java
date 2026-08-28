package com.personal.springlessons.model.dto.response;

import java.util.Objects;

import org.jspecify.annotations.Nullable;

public class MaxUploadSizeAdditionalDetailsDTO {

  private @Nullable String maxUploadSize;

  public MaxUploadSizeAdditionalDetailsDTO() {
    // no-args constructor for Jackson deserialization
  }

  public @Nullable String getMaxUploadSize() {
    return this.maxUploadSize;
  }

  public void setMaxUploadSize(@Nullable String maxUploadSize) {
    this.maxUploadSize = maxUploadSize;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof MaxUploadSizeAdditionalDetailsDTO that))
      return false;
    return Objects.equals(this.maxUploadSize, that.maxUploadSize);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.maxUploadSize);
  }

  @Override
  public String toString() {
    return "MaxUploadSizeAdditionalDetailsDTO{maxUploadSize='" + this.maxUploadSize + "'}";
  }
}
