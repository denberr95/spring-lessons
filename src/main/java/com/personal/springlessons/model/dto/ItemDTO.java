package com.personal.springlessons.model.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.personal.springlessons.util.Constants;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemDTO {

    private String id;

    @NotBlank(message = Constants.ERROR_MSG_NOT_BLANK)
    @NumberFormat(style = Style.NUMBER, pattern = "#,###.##")
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
