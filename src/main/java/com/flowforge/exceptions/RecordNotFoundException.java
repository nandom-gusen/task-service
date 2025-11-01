package com.flowforge.exceptions;

public class RecordNotFoundException extends RuntimeException {
    private String message;

    public RecordNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}