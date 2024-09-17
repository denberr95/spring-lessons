package com.personal.springlessons.exception;

import lombok.Getter;

@Getter
public class DuplicatedBarcodeException extends SpringLessonsApplicationException {

    private static final long serialVersionUID = 1L;
    private final String barcode;
    private final String id;

    public DuplicatedBarcodeException(String barcode, String id) {
        super(String.format("Item '%s' already registered with id '%s'", barcode, id));
        this.barcode = barcode;
        this.id = id;
    }
}
