package com.personal.springlessons.model.dto;

import java.math.BigDecimal;
import java.util.Objects;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import com.personal.springlessons.util.Constants;

import org.jspecify.annotations.Nullable;

public class ItemDTO {

  private @Nullable String id;

  @NotNull(message = Constants.ERROR_MSG_NOT_BLANK) @Positive(message = Constants.ERROR_MSG_POSITIVE_VALUE) @DecimalMin(value = Constants.S_VAL_0_01, inclusive = true,
      message = Constants.ERROR_MSG_MIN_VALUE)
  @DecimalMax(value = Constants.S_VAL_9999_99, inclusive = true,
      message = Constants.ERROR_MSG_MAX_VALUE)
  private BigDecimal price;

  @NotBlank(message = Constants.ERROR_MSG_NOT_BLANK) @Size(min = Constants.I_VAL_1, max = Constants.I_VAL_100,
      message = Constants.ERROR_MSG_LEN_VALIDATION)
  private String name;

  @Size(min = Constants.I_VAL_1, max = Constants.I_VAL_50,
      message = Constants.ERROR_MSG_LEN_VALIDATION)
  @NotBlank(message = Constants.ERROR_MSG_NOT_BLANK) private String barcode;

  public ItemDTO() {
    // no-args constructor for Jackson deserialization and subclass use
  }

  public @Nullable String getId() {
    return this.id;
  }

  public void setId(@Nullable String id) {
    this.id = id;
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
    if (!(o instanceof ItemDTO that))
      return false;
    return Objects.equals(this.id, that.id) && Objects.equals(this.price, that.price)
        && Objects.equals(this.name, that.name) && Objects.equals(this.barcode, that.barcode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.price, this.name, this.barcode);
  }

  @Override
  public String toString() {
    return "ItemDTO{" + "id='" + this.id + '\'' + ", price=" + this.price + ", name='" + this.name
        + '\'' + ", barcode='" + this.barcode + '\'' + '}';
  }
}
