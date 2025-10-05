package com.personal.springlessons.exception;

public class SpringLessonsApplicationException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public SpringLessonsApplicationException(String message) {
    super(message);
  }

  public SpringLessonsApplicationException(Throwable cause) {
    super(cause);
  }

  public SpringLessonsApplicationException(String message, Throwable cause) {
    super(message, cause);
  }
}
