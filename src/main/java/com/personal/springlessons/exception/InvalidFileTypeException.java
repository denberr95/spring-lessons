package com.personal.springlessons.exception;

import java.util.List;
import lombok.Getter;

@Getter
public class InvalidFileTypeException extends RuntimeException {

    private final String fileName;
    private final List<String> validFileTypes;

    public InvalidFileTypeException(String fileName, List<String> validFileTypes) {
        super("Unsupported file type !");
        this.fileName = fileName;
        this.validFileTypes = validFileTypes;
    }
}
