package com.personal.springlessons.exception.items;

public class DuplicatedBarcodeException extends RuntimeException {

  private static final long serialVersionUID = 1L;
  private final String barcode;
  private final String id;

  public DuplicatedBarcodeException(String barcode, String id) {
    super(String.format("Item '%s' already exists with id '%s'", barcode, id));
    this.barcode = barcode;
    this.id = id;
  }

  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  public String getBarcode() {
    return this.barcode;
  }

  public String getId() {
    return this.id;
  }
}
