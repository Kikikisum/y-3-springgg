package com.example.y3spring.exception;

public class NestedIOException extends RuntimeException{
    public NestedIOException() {
    }

    public NestedIOException(String message) {
        super(message);
    }

    public NestedIOException(String message, Throwable cause) {
        super(message, cause);
    }
}
