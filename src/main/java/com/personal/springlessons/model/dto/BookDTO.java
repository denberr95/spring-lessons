package com.personal.springlessons.model.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.personal.springlessons.util.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookDTO {

    private String id;

    @NotBlank(message = Constants.ERROR_MSG_NOT_BLANK)
    @Size(min = Constants.I_VAL_1, max = Constants.I_VAL_100,
            message = Constants.ERROR_MSG_LEN_VALIDATION)
    private String name;

    @NotNull(message = Constants.ERROR_MSG_NOT_BLANK)
    @Min(value = Constants.I_VAL_1, message = Constants.ERROR_MSG_MIN_VALUE)
    private Integer numberOfPages;

    @NotNull(message = Constants.ERROR_MSG_NOT_BLANK)
    private LocalDate publicationDate;
}
