package com.example.y3spring.jdbc.core;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface PreparedStatementCreator {

    /**
     * 创建新的语句
     * @param con
     * @return 返回语句
     * @throws SQLException
     */
    PreparedStatement createPreparedStatement(Connection con) throws SQLException;
}
