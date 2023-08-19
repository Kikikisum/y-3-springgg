package com.example.y3spring.jdbc.exception;

import java.sql.SQLException;

public class BadSqlGrammarException extends NonTransientDataAccessException{
    private String sql;


    public BadSqlGrammarException(String task, String sql, SQLException ex) {
        super(task + "; bad SQL grammar [" + sql + "]", ex);
        this.sql = sql;
    }

    public SQLException getSQLException() {
        return (SQLException) getCause();
    }

    public String getSql() {
        return this.sql;
    }

}
