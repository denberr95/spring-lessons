package com.personal.springlessons.exception.books;

public class DuplicatedBookException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final String name;
    private final String id;

    public DuplicatedBookException(String name, String id) {
        super(String.format("Book '%s' already exists with id '%s'", name, id));
        this.name = name;
        this.id = id;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getName() {
        return this.name;
    }

    public String getId() {
        return this.id;
    }
}
