package com.example.y3spring.transaction.dataSource;

import java.sql.Connection;

public interface ConnectionHandle{
    /**
     * 获取JDBC的数据库连接
     * @return
     */
    Connection getConnection();

    /**
     * 释放连接
     * @param con
     */
    void releaseConnection(Connection con);
}
