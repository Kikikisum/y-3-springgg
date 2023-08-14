package com.example.y3spring.jdbc.support;

import org.springframework.lang.Nullable;


public class SQLErrorCodes {
    @Nullable
    private String[] databaseProductNames;

    @Nullable
    private SQLExceptionTranslator customSqlExceptionTranslator;

    @Nullable
    public String[] getDatabaseProductNames() {
        return this.databaseProductNames;
    }

    public void setCustomSqlExceptionTranslator(@Nullable SQLExceptionTranslator customSqlExceptionTranslator) {
        this.customSqlExceptionTranslator = customSqlExceptionTranslator;
    }

    @Nullable
    public SQLExceptionTranslator getCustomSqlExceptionTranslator() {
        return this.customSqlExceptionTranslator;
    }

}
