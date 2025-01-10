package com.personal.springlessons.model.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import com.personal.springlessons.util.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemDTO {

    private String id;

    @NotNull(message = Constants.ERROR_MSG_NOT_BLANK)
    @Positive(message = Constants.ERROR_MSG_POSITIVE_VALUE)
    @DecimalMax(value = Constants.S_VAL_9999_99, inclusive = true, message = Constants.ERROR_MSG_MAX_VALUE)
    private BigDecimal price;

    @NotBlank(message = Constants.ERROR_MSG_NOT_BLANK)
    @Size(min = Constants.I_VAL_1, max = Constants.I_VAL_100,
            message = Constants.ERROR_MSG_LEN_VALIDATION)
    private String name;

    @Size(min = Constants.I_VAL_1, max = Constants.I_VAL_50,
            message = Constants.ERROR_MSG_LEN_VALIDATION)
    @NotBlank(message = Constants.ERROR_MSG_NOT_BLANK)
    private String barcode;
}
