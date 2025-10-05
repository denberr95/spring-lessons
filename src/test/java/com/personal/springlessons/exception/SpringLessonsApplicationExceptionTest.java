package com.personal.springlessons.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class SpringLessonsApplicationExceptionTest {

  @Test
  void testSpringLessonsApplicationExceptionWithMessage() {
    String errorMessage = "This is a custom exception message";
    SpringLessonsApplicationException exception =
        new SpringLessonsApplicationException(errorMessage);

    assertEquals(errorMessage, exception.getMessage());
    assertNull(exception.getCause());
  }

  @Test
  void testSpringLessonsApplicationExceptionWithCause() {
    Throwable cause = new Throwable("This is the cause");
    SpringLessonsApplicationException exception = new SpringLessonsApplicationException(cause);

    assertEquals(cause, exception.getCause());
    assertEquals("java.lang.Throwable: This is the cause", exception.getMessage());
  }

  @Test
  void testSpringLessonsApplicationExceptionWithMessageAndCause() {
    String errorMessage = "This is a custom exception message";
    Throwable cause = new Throwable("This is the cause");
    SpringLessonsApplicationException exception =
        new SpringLessonsApplicationException(errorMessage, cause);

    assertEquals(errorMessage, exception.getMessage());
    assertEquals(cause, exception.getCause());
  }
}
