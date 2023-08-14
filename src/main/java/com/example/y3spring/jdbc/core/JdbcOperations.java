package com.example.y3spring.jdbc.core;


import com.example.y3spring.jdbc.exception.DataAccessException;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Map;

public interface JdbcOperations {

    //--------------------------Execute----------------------------------
    /**
     * 简单的执行sql语句的方法
     * @param sql
     * @throws DataAccessException
     */
    void execute(String sql) throws DataAccessException;

    //--------------------------Query----------------------------------
    /**
     * 直接执行sql语句，通过RowMapper返回结果
     * @param sql
     * @param rowMapper
     * @return
     * @param <T>
     * @throws DataAccessException
     */
    <T> List<T> query(String sql, RowMapper<T> rowMapper) throws DataAccessException;

    /**
     * 根据sql语句创建PreparedStatement对象，通过RowMapper将结果返回到List
     * @param sql
     * @param pss
     * @param rowMapper
     * @return
     * @param <T>
     * @throws DataAccessException
     */
    <T> List<T> query(String sql, @Nullable PreparedStatementSetter pss, RowMapper<T> rowMapper) throws DataAccessException;

    /**
     * 用Object[]来设置sql的参数，使用RowMapper的回调方法返回数据
     * @param sql
     * @param args
     * @param argTypes
     * @param rowMapper
     * @return
     * @param <T>
     * @throws DataAccessException
     */
    <T> List<T> query(String sql, Object[] args, int[] argTypes, RowMapper<T> rowMapper) throws DataAccessException;

    /**
     * 用args参数来设置sql的参数，通过RowMapper返回一个Object类型的单行记录
     * @param sql
     * @param args
     * @param rowMapper
     * @return
     * @param <T>
     * @throws DataAccessException
     */
    @Nullable
    <T> T queryForObject(String sql, Object[] args, RowMapper<T> rowMapper) throws DataAccessException;

    /**
     *
     * @param sql
     * @param args
     * @param elementType
     * @return 返回一个多行数据的列表
     * @param <T>
     * @throws DataAccessException
     */
    <T>List<T> queryForList(String sql, Object[] args, Class<T> elementType)
            throws DataAccessException;

    //--------------------------Update----------------------------------

    /**
     * 最简单的update方法
     * @param sql  传入的sql语句
     * @return 返回受影响的行数
     * @throws DataAccessException
     */
    int update(String sql) throws DataAccessException;

    /**
     * 执行PreparedStatementCreator返回的语句
     * @param psc
     * @return 返回受影响的行数
     * @throws DataAccessException
     */
    int update(PreparedStatementCreator psc) throws DataAccessException;

    /**
     * 通过PreparedStatementSetter设置sql语句的参数
     * @param sql
     * @param pss
     * @return 返回受影响的行数
     * @throws DataAccessException
     */
    int update(String sql, @Nullable PreparedStatementSetter pss) throws DataAccessException;

    /**
     * 使用Object...设置sql语句的参数
     * @param sql
     * @param args
     * @return 返回受影响的行数
     * @throws DataAccessException
     */
    int update(String sql, @Nullable Object... args) throws DataAccessException;
}
