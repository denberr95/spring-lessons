package com.personal.springlessons.model.dto.response;

import java.util.Objects;

import org.jspecify.annotations.Nullable;

public class MissingRequestPartAdditionalDetailsDTO {

  private @Nullable String part;

  public MissingRequestPartAdditionalDetailsDTO() {
    // no-args constructor for Jackson deserialization
  }

  public @Nullable String getPart() {
    return this.part;
  }

  public void setPart(@Nullable String part) {
    this.part = part;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof MissingRequestPartAdditionalDetailsDTO that))
      return false;
    return Objects.equals(this.part, that.part);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.part);
  }

  @Override
  public String toString() {
    return "MissingRequestPartAdditionalDetailsDTO{part='" + this.part + "'}";
  }
}
