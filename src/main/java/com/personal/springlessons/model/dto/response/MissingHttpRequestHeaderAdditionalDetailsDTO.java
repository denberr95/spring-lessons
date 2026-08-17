package com.personal.springlessons.model.dto.response;

import java.util.Objects;

public class MissingHttpRequestHeaderAdditionalDetailsDTO {

  private String header;

  public MissingHttpRequestHeaderAdditionalDetailsDTO() {
    // no-args constructor for Jackson deserialization
  }

  public String getHeader() {
    return this.header;
  }

  public void setHeader(String header) {
    this.header = header;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof MissingHttpRequestHeaderAdditionalDetailsDTO that))
      return false;
    return Objects.equals(this.header, that.header);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.header);
  }

  @Override
  public String toString() {
    return "MissingHttpRequestHeaderAdditionalDetailsDTO{header='" + this.header + "'}";
  }
}
