package com.personal.springlessons.exception;

import lombok.Getter;

@Getter
public class DuplicatedBookException extends SpringLessonsApplicationException {

    private static final long serialVersionUID = 1L;
    private final String name;
    private final String id;

    public DuplicatedBookException(final String name, final String id) {
        super(String.format("Book '%s' is associated at id '%s'", name, id));
        this.name = name;
        this.id = id;
    }
}
