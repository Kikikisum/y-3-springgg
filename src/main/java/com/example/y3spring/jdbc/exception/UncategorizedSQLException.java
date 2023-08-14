package com.example.y3spring.jdbc.exception;

import com.example.y3spring.jdbc.exception.UncategorizedDataAccessException;
import org.springframework.lang.Nullable;

import java.sql.SQLException;

/**
 * 无法将SQLException分类为时引发的异常
 */

public class UncategorizedSQLException extends UncategorizedDataAccessException {

    /**
     * 导致该问题的sql语句
     */
    @Nullable
    private final String sql;

    public UncategorizedSQLException(String task, @Nullable String sql, SQLException ex) {
        super(task + "; uncategorized SQLException" + (sql != null ? " for SQL [" + sql + "]" : "") +
                "; SQL state [" + ex.getSQLState() + "]; error code [" + ex.getErrorCode() + "]; " +
                ex.getMessage(), ex);
        this.sql = sql;
    }

    public SQLException getSQLException() {
        return (SQLException) getCause();
    }

    @Nullable
    public String getSql() {
        return this.sql;
    }
}
