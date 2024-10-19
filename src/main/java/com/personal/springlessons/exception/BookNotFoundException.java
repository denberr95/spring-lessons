package com.personal.springlessons.exception;

import lombok.Getter;

@Getter
public class BookNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final String id;

    public BookNotFoundException(String id) {
        super(String.format("Book '%s' not found !", id));
        this.id = id;
    }
}
