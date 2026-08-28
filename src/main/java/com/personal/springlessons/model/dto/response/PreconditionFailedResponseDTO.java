package com.personal.springlessons.model.dto.response;

import java.util.Objects;

public class PreconditionFailedResponseDTO extends BaseErrorResponseDTO {

  private PreconditionFailedAdditionalDetailsDTO additionalData;

  public PreconditionFailedResponseDTO() {
    // no-args constructor for Jackson deserialization
  }

  public PreconditionFailedAdditionalDetailsDTO getAdditionalData() {
    return this.additionalData;
  }

  public void setAdditionalData(PreconditionFailedAdditionalDetailsDTO additionalData) {
    this.additionalData = additionalData;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof PreconditionFailedResponseDTO that))
      return false;
    if (!super.equals(o))
      return false;
    return Objects.equals(this.additionalData, that.additionalData);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), this.additionalData);
  }

  @Override
  public String toString() {
    return "PreconditionFailedResponseDTO{" + super.toString() + ", additionalData="
        + this.additionalData + '}';
  }
}
