package com.personal.springlessons.exception;

import java.util.List;

public class InvalidFileTypeException extends RuntimeException {

    private final String fileName;
    private final List<String> validFileTypes;

    public InvalidFileTypeException(String fileName, List<String> validFileTypes) {
        super("Unsupported file type !");
        this.fileName = fileName;
        this.validFileTypes = validFileTypes;
    }

    public String getFileName() {
        return this.fileName;
    }

    public List<String> getValidFileTypes() {
        return this.validFileTypes;
    }
}
