package com.example.y3spring.jdbc.exception;

import org.springframework.lang.Nullable;

import java.sql.SQLException;

public class CannotGetJdbcConnectionException extends DataAccessResourceFailureException {

    public CannotGetJdbcConnectionException(String msg) {
        super(msg);
    }

    public CannotGetJdbcConnectionException(String msg, @Nullable SQLException ex) {
        super(msg, ex);
    }
}
