package com.flowforge.exceptions;

public class ConflictException extends RuntimeException {
    private String message;

    public ConflictException(String message) {
        super(message);
        this.message = message;
    }
}
