package com.autotax.integration.exception;

public class ErrorResponse extends RuntimeException {
    private int status;

    public ErrorResponse(int status, String message) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}