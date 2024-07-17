package com.personal.springlessons.model.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.personal.springlessons.util.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookDTO {

    private String id;

    @NotBlank(message = Constants.ERROR_MSG_NOT_BLANK)
    @Size(min = Constants.LEN_1, max = Constants.LEN_100,
            message = Constants.ERROR_MSG_LEN_VALIDATION)
    private String name;

    @NotBlank(message = Constants.ERROR_MSG_NOT_BLANK)
    @Min(value = Constants.LEN_1, message = Constants.ERROR_MSG_MIN_VALUE)
    private Integer numberOfPages;

    @NotBlank(message = Constants.ERROR_MSG_NOT_BLANK)
    private LocalDate publicationDate;
}
