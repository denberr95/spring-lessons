package com.personal.springlessons.exception;

public class SpringLessonsApplicationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SpringLessonsApplicationException(final String message) {
        super(message);
    }
}
