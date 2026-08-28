package com.personal.springlessons.model.dto;

import java.util.List;
import java.util.Objects;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import com.personal.springlessons.util.Constants;

import org.jspecify.annotations.Nullable;

public class OrderItemsDTO {

  private @Nullable String id;

  private @Nullable Integer quantity;

  @Size(min = Constants.I_VAL_1, max = Constants.I_VAL_100,
      message = Constants.ERROR_MSG_LIST_SIZE_VALIDATION)
  @NotEmpty(message = Constants.ERROR_MSG_NOT_BLANK) @Valid private @Nullable List<ItemDTO> items;

  public OrderItemsDTO() {
    // no-args constructor for Jackson deserialization
  }

  public @Nullable String getId() {
    return this.id;
  }

  public void setId(@Nullable String id) {
    this.id = id;
  }

  public @Nullable Integer getQuantity() {
    return this.quantity;
  }

  public void setQuantity(@Nullable Integer quantity) {
    this.quantity = quantity;
  }

  public @Nullable List<ItemDTO> getItems() {
    return this.items;
  }

  public void setItems(@Nullable List<ItemDTO> items) {
    this.items = items;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof OrderItemsDTO that))
      return false;
    return Objects.equals(this.id, that.id) && Objects.equals(this.quantity, that.quantity)
        && Objects.equals(this.items, that.items);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.quantity, this.items);
  }

  @Override
  public String toString() {
    return "OrderItemsDTO{" + "id='" + this.id + '\'' + ", quantity=" + this.quantity + ", items="
        + this.items + '}';
  }
}
