package com.personal.springlessons.model.dto.response;

import java.util.Objects;

public class DuplicatedBookAdditionalDetailsDTO {

  private String orinalId;

  public DuplicatedBookAdditionalDetailsDTO(String orinalId) {
    this.orinalId = orinalId;
  }

  public DuplicatedBookAdditionalDetailsDTO() {}

  public String getOrinalId() {
    return this.orinalId;
  }

  public void setOrinalId(String orinalId) {
    this.orinalId = orinalId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.orinalId);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (this.getClass() != obj.getClass())
      return false;
    DuplicatedBookAdditionalDetailsDTO other = (DuplicatedBookAdditionalDetailsDTO) obj;
    return Objects.equals(this.orinalId, other.orinalId);
  }

  @Override
  public String toString() {
    return "DuplicatedBookAdditionalDetailsDTO [orinalId=" + this.orinalId + "]";
  }
}
