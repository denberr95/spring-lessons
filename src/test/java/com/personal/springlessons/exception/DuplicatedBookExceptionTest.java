package com.personal.springlessons.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class DuplicatedBookExceptionTest {

    @Test
    void testDuplicatedBookException() {
        String name = "Test Book";
        String id = "12345";
        DuplicatedBookException exception = new DuplicatedBookException(name, id);
        String expectedMessage = String.format("Book '%s' is associated at id '%s'", name, id);
        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(name, exception.getName());
        assertEquals(id, exception.getId());
    }
}
