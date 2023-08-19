package com.example.y3spring.jdbc.exception;

import org.springframework.lang.Nullable;

public class ConcurrencyFailureException extends DataAccessException {

    public ConcurrencyFailureException(String msg) {
        super(msg);
    }

    public ConcurrencyFailureException(String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }

}