package com.personal.springlessons.model.dto.response;

import java.util.Objects;

import com.personal.springlessons.model.dto.ItemDTO;
import com.personal.springlessons.model.lov.ItemStatus;

public class KafkaMessageItemDTO extends ItemDTO {

  private String idOrderItems;
  private ItemStatus itemStatus;

  public KafkaMessageItemDTO() {
    // no-args constructor for Jackson deserialization
  }

  public String getIdOrderItems() {
    return this.idOrderItems;
  }

  public void setIdOrderItems(String idOrderItems) {
    this.idOrderItems = idOrderItems;
  }

  public ItemStatus getItemStatus() {
    return this.itemStatus;
  }

  public void setItemStatus(ItemStatus itemStatus) {
    this.itemStatus = itemStatus;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof KafkaMessageItemDTO that))
      return false;
    return Objects.equals(this.idOrderItems, that.idOrderItems)
        && Objects.equals(this.itemStatus, that.itemStatus);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.idOrderItems, this.itemStatus);
  }

  @Override
  public String toString() {
    return "KafkaMessageItemDTO{" + super.toString() + ", idOrderItems='" + this.idOrderItems + '\''
        + ", itemStatus=" + this.itemStatus + '}';
  }
}
