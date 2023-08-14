package com.example.y3spring.jdbc.exception;

import com.example.y3spring.jdbc.exception.NonTransientDataAccessException;
import org.springframework.lang.Nullable;

public class UncategorizedDataAccessException extends NonTransientDataAccessException {

    public UncategorizedDataAccessException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
