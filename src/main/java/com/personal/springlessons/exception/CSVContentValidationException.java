package com.personal.springlessons.exception;

import java.util.List;
import com.personal.springlessons.model.dto.InvalidCsvDTO;
import lombok.Getter;

@Getter
public class CSVContentValidationException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final String fileName;
    private final transient List<InvalidCsvDTO> rows;

    public CSVContentValidationException(String fileName, List<InvalidCsvDTO> rows) {
        super(String.format("CSV file: %s is invalid", fileName));
        this.fileName = fileName;
        this.rows = rows;
    }
}
