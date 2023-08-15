package com.example.y3spring.jdbc.core;

import java.sql.ResultSet;

public interface ResultSetHandler<T> {
    /**
     * 用来处理结果集
     * @param rs
     * @return
     * @param <T>
     */
    <T> T handler(ResultSet rs);
}
