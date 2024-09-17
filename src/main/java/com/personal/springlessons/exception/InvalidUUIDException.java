package com.personal.springlessons.exception;

import lombok.Getter;

@Getter
public class InvalidUUIDException extends SpringLessonsApplicationException {

    private static final long serialVersionUID = 1L;
    private final String id;

    public InvalidUUIDException(String id) {
        super(String.format("ID '%s' malformed, is not a valid UUID", id));
        this.id = id;
    }
}
