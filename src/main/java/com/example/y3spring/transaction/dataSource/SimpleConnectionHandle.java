package com.example.y3spring.transaction.dataSource;

import org.springframework.util.Assert;

import java.sql.Connection;

public class SimpleConnectionHandle implements ConnectionHandle{
    private final Connection connection;


    public SimpleConnectionHandle(Connection connection) {
        Assert.notNull(connection, "连接为空！！");
        this.connection = connection;
    }

    @Override
    public Connection getConnection() {
        return this.connection;
    }

    /**
     * 不用进行释放，内容为空
     * @param con
     */
    @Override
    public void releaseConnection(Connection con) {
    }


    @Override
    public String toString() {
        return "SimpleConnectionHandle: " + this.connection;
    }
}
