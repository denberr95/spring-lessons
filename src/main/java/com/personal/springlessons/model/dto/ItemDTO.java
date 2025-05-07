package com.personal.springlessons.model.dto;

import java.math.BigDecimal;
import java.util.Objects;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import com.personal.springlessons.util.Constants;

public class ItemDTO {

    private String id;

    @NotNull(message = Constants.ERROR_MSG_NOT_BLANK)
    @Positive(message = Constants.ERROR_MSG_POSITIVE_VALUE)
    @DecimalMax(value = Constants.S_VAL_9999_99, inclusive = true,
            message = Constants.ERROR_MSG_MAX_VALUE)
    private BigDecimal price;

    @NotBlank(message = Constants.ERROR_MSG_NOT_BLANK)
    @Size(min = Constants.I_VAL_1, max = Constants.I_VAL_100,
            message = Constants.ERROR_MSG_LEN_VALIDATION)
    private String name;

    @Size(min = Constants.I_VAL_1, max = Constants.I_VAL_50,
            message = Constants.ERROR_MSG_LEN_VALIDATION)
    @NotBlank(message = Constants.ERROR_MSG_NOT_BLANK)
    private String barcode;

    public ItemDTO(String id,
            @NotNull(message = Constants.ERROR_MSG_NOT_BLANK) @Positive(
                    message = Constants.ERROR_MSG_POSITIVE_VALUE) @DecimalMax(
                            value = Constants.S_VAL_9999_99, inclusive = true,
                            message = Constants.ERROR_MSG_MAX_VALUE) BigDecimal price,
            @NotBlank(message = Constants.ERROR_MSG_NOT_BLANK) @Size(min = Constants.I_VAL_1,
                    max = Constants.I_VAL_100,
                    message = Constants.ERROR_MSG_LEN_VALIDATION) String name,
            @Size(min = Constants.I_VAL_1, max = Constants.I_VAL_100,
                    message = Constants.ERROR_MSG_LEN_VALIDATION) @NotBlank(
                            message = Constants.ERROR_MSG_NOT_BLANK) String barcode) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.barcode = barcode;
    }

    public ItemDTO() {}

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
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
    public int hashCode() {
        return Objects.hash(this.id, this.price, this.name, this.barcode);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        ItemDTO other = (ItemDTO) obj;
        return Objects.equals(this.id, other.id) && Objects.equals(this.price, other.price)
                && Objects.equals(this.name, other.name)
                && Objects.equals(this.barcode, other.barcode);
    }

    @Override
    public String toString() {
        return "ItemDTO [id=" + this.id + ", price=" + this.price + ", name=" + this.name
                + ", barcode=" + this.barcode + "]";
    }
}
