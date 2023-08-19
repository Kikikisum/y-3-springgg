package com.example.y3spring.jdbc.core;

import com.example.y3spring.jdbc.exception.DataAccessException;
import com.example.y3spring.jdbc.support.JdbcAccessor;
import com.example.y3spring.transaction.DataSource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate extends JdbcAccessor implements JdbcOperations {

    // 定义参数变量(数据源、连接对象、执行者对象、结果集对象)
    private DataSource dataSource;

    private Connection con;

    private PreparedStatement pst;

    private ResultSet rs;

    public JdbcTemplate(){

    }

    public JdbcTemplate(DataSource dataSource) {
        setDataSource(dataSource);
        afterPropertiesSet();
    }

    public JdbcTemplate(DataSource dataSource, boolean lazyInit) {
        setDataSource(dataSource);
        setLazyInit(lazyInit);
        afterPropertiesSet();
    }

    @Override
    public Object query(String sql, ResultSetHandler rsh, Object... args) {
        int result = 0;
        try {
            if (dataSource == null) {
                throw new NullPointerException("DataSource can not empty!");
            }
            // 获取数据库连接
            con = dataSource.getConnection();
            // 通过连接获取执行者对象，同时对sql语句进行预编译
            pst = con.prepareStatement(sql);
            // 通过执行者获取源信息对象
            ParameterMetaData parameterMetaData = pst.getParameterMetaData();
            // 获取参数格式
            int count = parameterMetaData.getParameterCount();
            if (count != args.length) {
                throw new RuntimeException("参数个数不匹配!");
            }
            for (int i = 0; i < args.length; i++) {
                pst.setObject(i + 1, args[i]);
            }
            result = pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            DataSourceUtils.close(con,pst);
        }

        return result;
    }

    @Override
    public <T> List<T> queryForListObject(String sql, ResultSetHandler<T> rsh, Object...objs){
        List<T> list = new ArrayList<>();
        try {
            if (dataSource == null) {
                throw new NullPointerException("DataSource can not empty!");
            }
            con = dataSource.getConnection();
            pst = con.prepareStatement(sql);
            ParameterMetaData parameterMetaData = pst.getParameterMetaData();
            int count = parameterMetaData.getParameterCount();
            if (count != objs.length){
                throw new RuntimeException("参数个数不匹配！");
            }
            // 为sql语句占位符赋值
            for (int i = 0; i < objs.length; i++) {
                pst.setObject(i+1,objs[i]);
            }
            rs = pst.executeQuery();
            // 通过BeanListHandler方式对结果进行处理
            list = rsh.handler(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DataSourceUtils.close(con,pst);
        }
        return list;
    }

    @Override
    public <T> T queryForObject(String sql, ResultSetHandler<T> rsh,Object...objs) throws DataAccessException {
        //4.定义int类型变量，用于接收增删改后影响的行数
        T obj = null;
        try {
            if (dataSource == null) {
                throw new NullPointerException("DataSource can not empty!");
            }
            // 获取一个数据库连接
            con = dataSource.getConnection();
            pst = con.prepareStatement(sql);
            ParameterMetaData parameterMetaData = pst.getParameterMetaData();
            int count = parameterMetaData.getParameterCount();
            if (count != objs.length){
                throw new RuntimeException("参数个数不匹配！");
            }
            for (int i = 0; i < objs.length; i++) {
                pst.setObject(i+1,objs[i]);
            }
            rs = pst.executeQuery();
            obj = rsh.handler(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            // 释放资源
            DataSourceUtils.close(con,pst);
        }
        return obj;
    }

    @Override
    public int update(String sql, Object... args) throws DataAccessException {
        int result = 0;
        try {
            if (dataSource == null) {
                throw new NullPointerException("DataSource can not empty!");
            }
            // 获取数据库连接
            con = dataSource.getConnection();
            // 通过连接获取执行者对象，同时对sql语句进行预编译
            pst = con.prepareStatement(sql);
            // 通过执行者获取源信息对象
            ParameterMetaData parameterMetaData = pst.getParameterMetaData();
            // 获取参数格式
            int count = parameterMetaData.getParameterCount();
            if (count != args.length) {
                throw new RuntimeException("参数个数不匹配!");
            }
            for (int i = 0; i < args.length; i++) {
                pst.setObject(i + 1, args[i]);
            }
            result = pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            DataSourceUtils.close(con,pst);
        }

        return result;
    }
}
