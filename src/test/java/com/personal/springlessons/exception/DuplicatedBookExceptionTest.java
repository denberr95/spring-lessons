package com.personal.springlessons.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class DuplicatedBookExceptionTest {

    @Test
    void testDuplicatedBookException() {
        String name = "Test Book";
        LocalDate publicationDate = LocalDate.of(2023, 1, 1);
        String id = "12345";
        DuplicatedBookException exception = new DuplicatedBookException(name, publicationDate, id);
        String expectedMessage =
                String.format("Book '%s' was publicated in '%s' and is associated at id '%s'", name,
                        publicationDate, id);
        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(name, exception.getName());
        assertEquals(publicationDate, exception.getPublicationDate());
        assertEquals(id, exception.getId());
    }
}
