package com.example.y3spring.jdbc.support;

import com.example.y3spring.jdbc.exception.DataAccessException;
import org.springframework.lang.Nullable;

import java.sql.SQLException;

/**
 * SQLException和DataAccessException之间作转换的接口
 */
@FunctionalInterface
public interface SQLExceptionTranslator {

    /**
     * @param task
     * @param sql
     * @param ex
     * @return
     */
    @Nullable
    DataAccessException translate(String task, @Nullable String sql, SQLException ex);
}
