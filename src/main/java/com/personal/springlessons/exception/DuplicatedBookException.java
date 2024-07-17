package com.personal.springlessons.exception;

import java.time.LocalDate;
import lombok.Getter;

@Getter
public class DuplicatedBookException extends SpringLessonsApplicationException {

    private static final long serialVersionUID = 1L;
    private final String name;
    private final LocalDate publicationDate;
    private final String id;

    public DuplicatedBookException(final String name, final LocalDate publicationDate,
            final String id) {
        super(String.format("Book '%s' was publicated in '%s' and is associated at id '%s'", name,
                publicationDate, id));
        this.name = name;
        this.publicationDate = publicationDate;
        this.id = id;
    }
}
