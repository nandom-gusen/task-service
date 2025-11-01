package com.flowforge.exceptions;

public class TooManyRequestException extends RuntimeException {
    private String message;

    public TooManyRequestException(String message) {
        super(message);
        this.message = message;
    }
}
