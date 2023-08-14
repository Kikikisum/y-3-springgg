package com.example.y3spring.jdbc.core;

import org.springframework.lang.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 将每一行数据进行封装成自定义类的接口
 * @param <T>
 */
public interface RowMapper<T> {
    /**
     * 实现对自定义类的包装
     * @param rs
     * @param rowNum
     * @return
     * @throws SQLException
     */
    @Nullable
    T mapRow(ResultSet rs, int rowNum) throws SQLException;
}
