package com.personal.springlessons.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class InvalidUUIDExceptionTest {

    @Test
    void testInvalidUUIDException() {
        String id = "ABCD";
        InvalidUUIDException exception = new InvalidUUIDException(id);
        String expectedMessage = String.format("ID '%s' is malformed, is not a valid UUID", id);
        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(id, exception.getId());
    }
}
