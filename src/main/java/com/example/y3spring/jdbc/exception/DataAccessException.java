package com.example.y3spring.jdbc.exception;

import org.springframework.core.NestedRuntimeException;
import org.springframework.lang.Nullable;

public abstract class DataAccessException extends NestedRuntimeException {

    public DataAccessException(String msg) {
        super(msg);
    }

    public DataAccessException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
