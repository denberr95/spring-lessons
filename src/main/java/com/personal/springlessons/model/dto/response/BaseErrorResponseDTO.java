package com.personal.springlessons.model.dto.response;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

import com.personal.springlessons.model.lov.DomainCategory;

public class BaseErrorResponseDTO {

  private OffsetDateTime timestamp = OffsetDateTime.now(ZoneOffset.UTC);
  private DomainCategory category;
  private String message;

  public BaseErrorResponseDTO() {
    // no-args constructor for Jackson deserialization; timestamp initialised at field declaration
  }

  public OffsetDateTime getTimestamp() {
    return this.timestamp;
  }

  public void setTimestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public DomainCategory getCategory() {
    return this.category;
  }

  public void setCategory(DomainCategory category) {
    this.category = category;
  }

  public String getMessage() {
    return this.message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof BaseErrorResponseDTO that))
      return false;
    return Objects.equals(this.timestamp, that.timestamp)
        && Objects.equals(this.category, that.category)
        && Objects.equals(this.message, that.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.timestamp, this.category, this.message);
  }

  @Override
  public String toString() {
    return "BaseErrorResponseDTO{" + "timestamp=" + this.timestamp + ", category=" + this.category
        + ", message='" + this.message + '\'' + '}';
  }
}
