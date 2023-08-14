package com.example.y3spring.jdbc.support;

import com.example.y3spring.jdbc.exception.DataAccessException;
import org.springframework.lang.Nullable;

import javax.sql.DataSource;
import java.sql.SQLException;

public class SQLErrorCodeSQLExceptionTranslator extends AbstractFallbackSQLExceptionTranslator  {

    @Nullable
    private SQLErrorCodes sqlErrorCodes;

    public SQLErrorCodeSQLExceptionTranslator() {
        setFallbackTranslator(new SQLExceptionSubclassTranslator());
    }

    public SQLErrorCodeSQLExceptionTranslator(DataSource dataSource) {
        this();
        setDataSource(dataSource);
    }

    /**
     * 为给定的数据库产品名称创建SQL错误代码转换器
     * @param dbName
     */
    public SQLErrorCodeSQLExceptionTranslator(String dbName) {
        this();
        setDatabaseProductName(dbName);
    }

    public void setDataSource(DataSource dataSource) {
        this.sqlErrorCodes = SQLErrorCodesFactory.getInstance().getErrorCodes(dataSource);
    }

    public void setDatabaseProductName(String dbName) {
        this.sqlErrorCodes = SQLErrorCodesFactory.getInstance().getErrorCodes(dbName);
    }

    /**
     * 还未实现
     * @param task 正在尝试的任务的可读文本
     * @param sql 导致问题的sql查询或更新
     * @param ex
     * @return
     */
    @Override
    protected DataAccessException doTranslate(String task, String sql, SQLException ex) {
        return null;
    }
}
