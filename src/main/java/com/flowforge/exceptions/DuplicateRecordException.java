package com.flowforge.exceptions;

public class DuplicateRecordException extends RuntimeException {
    private String message;

    public DuplicateRecordException(String message) {
        super(message);
        this.message = message;
    }
}