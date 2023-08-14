package com.example.y3spring.jdbc.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface PreparedStatementSetter {

    /**
     * 通过ps设置参数值
     * @param ps
     * @throws SQLException
     */
    void setValues(PreparedStatement ps) throws SQLException;
}
