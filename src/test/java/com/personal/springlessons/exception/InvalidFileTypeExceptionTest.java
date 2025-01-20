package com.personal.springlessons.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;


class InvalidFileTypeExceptionTest {

    @Test
    void testInvalidFileTypeExceptionMessage() {
        String fileName = "test.txt";
        List<String> validFileTypes = Arrays.asList("jpg", "png");
        InvalidFileTypeException exception = assertThrows(InvalidFileTypeException.class, () -> {
            throw new InvalidFileTypeException(fileName, validFileTypes);
        });
        assertEquals("Unsupported file type !", exception.getMessage());
    }

    @Test
    void testInvalidFileTypeExceptionFileName() {
        String fileName = "test.txt";
        List<String> validFileTypes = Arrays.asList("jpg", "png");
        InvalidFileTypeException exception = new InvalidFileTypeException(fileName, validFileTypes);
        assertEquals(fileName, exception.getFileName());
    }

    @Test
    void testInvalidFileTypeExceptionValidFileTypes() {
        String fileName = "test.txt";
        List<String> validFileTypes = Arrays.asList("jpg", "png");
        InvalidFileTypeException exception = new InvalidFileTypeException(fileName, validFileTypes);
        assertEquals(validFileTypes, exception.getValidFileTypes());
    }
}
