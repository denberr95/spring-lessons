package com.personal.springlessons.model.dto.response;

import java.util.Objects;

public class ConcurrentUpdateAdditionalDetailsDTO {

  private String id;
  private String version;

  public ConcurrentUpdateAdditionalDetailsDTO(String id, String version) {
    this.id = id;
    this.version = version;
  }

  public ConcurrentUpdateAdditionalDetailsDTO() {}

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getVersion() {
    return this.version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.version);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (this.getClass() != obj.getClass())
      return false;
    ConcurrentUpdateAdditionalDetailsDTO other = (ConcurrentUpdateAdditionalDetailsDTO) obj;
    return Objects.equals(this.id, other.id) && Objects.equals(this.version, other.version);
  }

  @Override
  public String toString() {
    return "ConcurrentUpdateAdditionalDetailsDTO [version=" + this.version + "]";
  }
}
