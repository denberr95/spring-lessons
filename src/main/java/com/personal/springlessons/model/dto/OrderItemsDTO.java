package com.personal.springlessons.model.dto;

import java.util.List;
import java.util.Objects;

public class OrderItemsDTO {

  private String id;

  private Integer quantity;

  private List<ItemDTO> items;

  public OrderItemsDTO(String id, Integer quantity, List<ItemDTO> items) {
    this.id = id;
    this.quantity = quantity;
    this.items = items;
  }

  public OrderItemsDTO() {}

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Integer getQuantity() {
    return this.quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public List<ItemDTO> getItems() {
    return this.items;
  }

  public void setItems(List<ItemDTO> items) {
    this.items = items;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.quantity, this.items);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (this.getClass() != obj.getClass())
      return false;
    OrderItemsDTO other = (OrderItemsDTO) obj;
    return Objects.equals(this.id, other.id) && Objects.equals(this.quantity, other.quantity)
        && Objects.equals(this.items, other.items);
  }

  @Override
  public String toString() {
    return "OrderItemsDTO [id=" + this.id + ", quantity=" + this.quantity + ", items=" + this.items
        + "]";
  }
}
