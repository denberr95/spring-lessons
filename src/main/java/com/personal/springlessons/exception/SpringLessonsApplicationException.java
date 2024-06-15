package com.personal.springlessons.exception;

public class SpringLessonsApplicationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SpringLessonsApplicationException(final String message) {
        super(message);
    }

    public SpringLessonsApplicationException(final Throwable cause) {
        super(cause);
    }

    public SpringLessonsApplicationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
