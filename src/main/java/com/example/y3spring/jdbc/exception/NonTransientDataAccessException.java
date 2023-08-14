package com.example.y3spring.jdbc.exception;

import org.springframework.lang.Nullable;

@SuppressWarnings("serial")
public class NonTransientDataAccessException  extends DataAccessException {

    public NonTransientDataAccessException(String msg) {
        super(msg);
    }

    public NonTransientDataAccessException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
