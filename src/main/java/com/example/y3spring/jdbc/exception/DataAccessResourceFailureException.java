package com.example.y3spring.jdbc.exception;

import org.springframework.lang.Nullable;

public class DataAccessResourceFailureException extends NonTransientDataAccessException {

    public DataAccessResourceFailureException(String msg) {
        super(msg);
    }

    public DataAccessResourceFailureException(String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
