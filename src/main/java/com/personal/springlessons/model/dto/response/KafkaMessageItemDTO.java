package com.personal.springlessons.model.dto.response;

import java.util.Objects;

import com.personal.springlessons.model.dto.ItemDTO;
import com.personal.springlessons.model.lov.ItemStatus;

import org.jspecify.annotations.Nullable;

public class KafkaMessageItemDTO extends ItemDTO {

  private @Nullable String idOrderItems;
  private @Nullable ItemStatus itemStatus;

  public KafkaMessageItemDTO() {
    // no-args constructor for Jackson deserialization
  }

  public @Nullable String getIdOrderItems() {
    return this.idOrderItems;
  }

  public void setIdOrderItems(@Nullable String idOrderItems) {
    this.idOrderItems = idOrderItems;
  }

  public @Nullable ItemStatus getItemStatus() {
    return this.itemStatus;
  }

  public void setItemStatus(@Nullable ItemStatus itemStatus) {
    this.itemStatus = itemStatus;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof KafkaMessageItemDTO that))
      return false;
    if (!super.equals(o))
      return false;
    return Objects.equals(this.idOrderItems, that.idOrderItems)
        && Objects.equals(this.itemStatus, that.itemStatus);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), this.idOrderItems, this.itemStatus);
  }

  @Override
  public String toString() {
    return "KafkaMessageItemDTO{" + super.toString() + ", idOrderItems='" + this.idOrderItems + '\''
        + ", itemStatus=" + this.itemStatus + '}';
  }
}
