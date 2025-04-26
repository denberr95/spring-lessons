package com.personal.springlessons.model.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.personal.springlessons.model.lov.Genre;
import com.personal.springlessons.util.Constants;

public record BookDTO(

        String id,

        @NotBlank(message = Constants.ERROR_MSG_NOT_BLANK) @Size(min = Constants.I_VAL_1,
                max = Constants.I_VAL_100,
                message = Constants.ERROR_MSG_LEN_VALIDATION) String name,

        @NotNull(message = Constants.ERROR_MSG_NOT_BLANK) @Min(value = Constants.I_VAL_1,
                message = Constants.ERROR_MSG_MIN_VALUE) Integer numberOfPages,

        @NotNull(message = Constants.ERROR_MSG_NOT_BLANK) LocalDate publicationDate,

        @NotNull(message = Constants.ERROR_MSG_NOT_BLANK) Genre genre

) {
}
