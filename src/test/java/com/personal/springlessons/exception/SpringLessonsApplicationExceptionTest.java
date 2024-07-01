package com.personal.springlessons.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SpringLessonsApplicationExceptionTest {

    @Test
    @DisplayName("Test Application Exception with Message")
    void testSpringLessonsApplicationExceptionWithMessage() {
        String errorMessage = "This is a custom exception message";
        SpringLessonsApplicationException exception =
                new SpringLessonsApplicationException(errorMessage);

        assertEquals(errorMessage, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Test Application Exception with Cause")
    void testSpringLessonsApplicationExceptionWithCause() {
        Throwable cause = new Throwable("This is the cause");
        SpringLessonsApplicationException exception = new SpringLessonsApplicationException(cause);

        assertEquals(cause, exception.getCause());
        assertEquals("java.lang.Throwable: This is the cause", exception.getMessage());
    }

    @Test
    @DisplayName("Test Application Exception with Message and Cause")
    void testSpringLessonsApplicationExceptionWithMessageAndCause() {
        String errorMessage = "This is a custom exception message";
        Throwable cause = new Throwable("This is the cause");
        SpringLessonsApplicationException exception =
                new SpringLessonsApplicationException(errorMessage, cause);

        assertEquals(errorMessage, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
