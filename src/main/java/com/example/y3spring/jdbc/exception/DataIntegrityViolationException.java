package com.example.y3spring.jdbc.exception;

public class DataIntegrityViolationException extends NonTransientDataAccessException {

    public DataIntegrityViolationException(String msg) {
        super(msg);
    }

    public DataIntegrityViolationException(String msg, Throwable cause) {
        super(msg, cause);
    }

}