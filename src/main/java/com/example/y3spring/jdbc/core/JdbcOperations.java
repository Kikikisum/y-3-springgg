package com.example.y3spring.jdbc.core;


import com.example.y3spring.jdbc.exception.DataAccessException;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Map;

public interface JdbcOperations {

    //--------------------------Query----------------------------------


    /**
     * 查询一条记录并封装对象的方法
     * @param sql
     * @param rsh
     * @param objs
     * @return
     * @param <T>
     * @throws DataAccessException
     */
    public <T> T queryForObject(String sql, ResultSetHandler<T> rsh,Object...objs) throws DataAccessException;

    public Object query(String sql, ResultSetHandler rsh, Object... args);

    //--------------------------Update----------------------------------


    /**
     * 使用Object...设置sql语句的参数
     * @param sql
     * @param args
     * @return 返回受影响的行数
     * @throws DataAccessException
     */
    int update(String sql, @Nullable Object... args) throws DataAccessException;

    /**
     * 用于将多条记录封装成自定义对象并添加到集合并返回
     * @param sql
     * @param rsh
     * @param objs
     * @return
     * @param <T>
     */
    public <T> List<T> queryForListObject(String sql, ResultSetHandler<T> rsh, Object...objs);
}
