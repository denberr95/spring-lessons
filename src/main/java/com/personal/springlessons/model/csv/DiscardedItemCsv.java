package com.personal.springlessons.model.csv;

import java.math.BigDecimal;
import java.util.Objects;

import com.opencsv.bean.CsvBindByName;

public class DiscardedItemCsv {

  @CsvBindByName
  private String idOrderItems;

  @CsvBindByName
  private String idOrderItemsOriginal;

  @CsvBindByName
  private String idItem;

  @CsvBindByName
  private BigDecimal price;

  @CsvBindByName
  private String name;

  @CsvBindByName
  private String barcode;

  public DiscardedItemCsv() {
    // Required by opencsv for reflection-based bean instantiation
  }

  public String getIdOrderItems() {
    return this.idOrderItems;
  }

  public void setIdOrderItems(String idOrderItems) {
    this.idOrderItems = idOrderItems;
  }

  public String getIdOrderItemsOriginal() {
    return this.idOrderItemsOriginal;
  }

  public void setIdOrderItemsOriginal(String idOrderItemsOriginal) {
    this.idOrderItemsOriginal = idOrderItemsOriginal;
  }

  public String getIdItem() {
    return this.idItem;
  }

  public void setIdItem(String idItem) {
    this.idItem = idItem;
  }

  public BigDecimal getPrice() {
    return this.price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getBarcode() {
    return this.barcode;
  }

  public void setBarcode(String barcode) {
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
