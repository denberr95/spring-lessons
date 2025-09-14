package com.personal.springlessons.exception.books;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class BookNotFoundExceptionTest {

    @Test
    void testBookNotFoundException() {
        String id = "12345";
        BookNotFoundException exception = new BookNotFoundException(id);
        String expectedMessage = String.format("Book '%s' not found !", id);
        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(id, exception.getId());
    }
}
