package com.flowforge.exceptions;

public class TaskFlowException extends RuntimeException {
    private String message;

    public TaskFlowException(String message) {
        super(message);
        this.message = message;
    }
}
