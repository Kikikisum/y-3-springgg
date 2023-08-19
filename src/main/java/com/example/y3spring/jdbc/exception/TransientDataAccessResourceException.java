package com.example.y3spring.jdbc.exception;

public class TransientDataAccessResourceException extends DataAccessException {

    public TransientDataAccessResourceException(String msg) {
        super(msg);
    }

    public TransientDataAccessResourceException(String msg, Throwable cause) {
        super(msg, cause);
    }

}