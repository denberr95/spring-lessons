package com.personal.springlessons.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CsvRowValidationDTO {

    private String field;

    private Object value;

    private String message;
}
