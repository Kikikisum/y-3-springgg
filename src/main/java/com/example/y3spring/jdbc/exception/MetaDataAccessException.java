package com.example.y3spring.jdbc.exception;

import org.springframework.core.NestedCheckedException;

public class MetaDataAccessException extends NestedCheckedException {

    public MetaDataAccessException(String msg) {
        super(msg);
    }

    public MetaDataAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
