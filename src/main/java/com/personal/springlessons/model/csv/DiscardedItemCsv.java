package com.personal.springlessons.model.csv;

import java.math.BigDecimal;
import java.util.Objects;

import com.opencsv.bean.CsvBindByName;

import org.jspecify.annotations.Nullable;

public class DiscardedItemCsv {

  @CsvBindByName
  private @Nullable String idOrderItems;

  @CsvBindByName
  private @Nullable String idOrderItemsOriginal;

  @CsvBindByName
  private @Nullable String idItem;

  @CsvBindByName
  private @Nullable BigDecimal price;

  @CsvBindByName
  private @Nullable String name;

  @CsvBindByName
  private @Nullable String barcode;

  public DiscardedItemCsv() {
    // Required by opencsv for reflection-based bean instantiation
  }

  public @Nullable String getIdOrderItems() {
    return this.idOrderItems;
  }

  public void setIdOrderItems(@Nullable String idOrderItems) {
    this.idOrderItems = idOrderItems;
  }

  public @Nullable String getIdOrderItemsOriginal() {
    return this.idOrderItemsOriginal;
  }

  public void setIdOrderItemsOriginal(@Nullable String idOrderItemsOriginal) {
    this.idOrderItemsOriginal = idOrderItemsOriginal;
  }

  public @Nullable String getIdItem() {
    return this.idItem;
  }

  public void setIdItem(@Nullable String idItem) {
    this.idItem = idItem;
  }

  public @Nullable BigDecimal getPrice() {
    return this.price;
  }

  public void setPrice(@Nullable BigDecimal price) {
    this.price = price;
  }

  public @Nullable String getName() {
    return this.name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public @Nullable String getBarcode() {
    return this.barcode;
  }

  public void setBarcode(@Nullable String barcode) {
    this.barcode = barcode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof DiscardedItemCsv that))
      return false;
    return Objects.equals(this.idOrderItems, that.idOrderItems)
        && Objects.equals(this.idOrderItemsOriginal, that.idOrderItemsOriginal)
        && Objects.equals(this.idItem, that.idItem) && Objects.equals(this.price, that.price)
        && Objects.equals(this.name, that.name) && Objects.equals(this.barcode, that.barcode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.idOrderItems, this.idOrderItemsOriginal, this.idItem, this.price,
        this.name, this.barcode);
  }

  @Override
  public String toString() {
    return "DiscardedItemCsv{" + "idOrderItems='" + this.idOrderItems + '\''
        + ", idOrderItemsOriginal='" + this.idOrderItemsOriginal + '\'' + ", idItem='" + this.idItem
        + '\'' + ", price=" + this.price + ", name='" + this.name + '\'' + ", barcode='"
        + this.barcode + '\'' + '}';
  }
}
