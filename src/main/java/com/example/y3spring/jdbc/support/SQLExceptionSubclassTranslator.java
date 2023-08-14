package com.example.y3spring.jdbc.support;

import com.example.y3spring.jdbc.exception.DataAccessException;

import java.sql.SQLException;

public class SQLExceptionSubclassTranslator extends AbstractFallbackSQLExceptionTranslator {
    public SQLExceptionSubclassTranslator() {
        setFallbackTranslator(new SQLStateSQLExceptionTranslator());
    }

    @Override
    protected DataAccessException doTranslate(String task, String sql, SQLException ex) {
        return null;
    }
}
