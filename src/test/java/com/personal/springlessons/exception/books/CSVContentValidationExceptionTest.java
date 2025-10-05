package com.personal.springlessons.exception.books;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.List;

import com.personal.springlessons.model.dto.InvalidCsvDTO;

import org.junit.jupiter.api.Test;

class CSVContentValidationExceptionTest {

  @Test
  void testCSVContentValidationException() {
    String fileName = "test.csv";
    List<InvalidCsvDTO> rows = Arrays.asList(new InvalidCsvDTO(), new InvalidCsvDTO());

    CSVContentValidationException exception = new CSVContentValidationException(fileName, rows);

    assertNotNull(exception);
    assertEquals(fileName, exception.getFileName());
    assertEquals(rows, exception.getRows());
    assertEquals("CSV file: test.csv is invalid", exception.getMessage());
  }
}
