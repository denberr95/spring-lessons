package com.personal.springlessons.model.dto;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InvalidCsvDTO {
    
    private Integer row;
    
    private List<CsvRowValidationDTO> validations;

}
