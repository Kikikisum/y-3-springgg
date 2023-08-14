package com.example.y3spring.jdbc.support;

import com.example.y3spring.jdbc.exception.DataAccessException;

import java.sql.SQLException;

public class SQLStateSQLExceptionTranslator extends AbstractFallbackSQLExceptionTranslator{

    /**
     * 还未实现
     * @param task 正在尝试的任务的可读文本
     * @param sql 导致问题的sql查询或更新
     * @param ex
     * @return
     */
    @Override
    protected DataAccessException doTranslate(String task, String sql, SQLException ex) {
        return null;
    }
}
