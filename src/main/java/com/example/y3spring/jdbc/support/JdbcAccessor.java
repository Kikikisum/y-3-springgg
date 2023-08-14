package com.example.y3spring.jdbc.support;

import com.example.y3spring.beans.factory.InitializingBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import javax.sql.DataSource;

/**
 * 父类是JdbcAccessor为子类JdbcTemplate提供了一些访问数据库时使用的公共属性
 */
public class JdbcAccessor implements InitializingBean {

    protected final Log logger = LogFactory.getLog(getClass());

    @Nullable
    private DataSource dataSource;

    @Nullable
    private volatile SQLExceptionTranslator exceptionTranslator;

    private boolean lazyInit = true;


    /**
     * 设置JDBC DataSource以从中获取连接
     * @param dataSource
     */
    public void setDataSource(@Nullable DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     *
     * @return 返回此模板使用的DataSource
     */
    @Nullable
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * 获取DataSource
     * @return 返回不为null的Datasource
     */
    protected DataSource obtainDataSource() {
        DataSource dataSource = getDataSource();
        Assert.state(dataSource != null, "No DataSource set");
        return dataSource;
    }

    /**
     * 设置翻译器的名字
     * @param dbName
     */
    public void setDatabaseProductName(String dbName) {
        this.exceptionTranslator = new SQLErrorCodeSQLExceptionTranslator(dbName);
    }

    /**
     * 如果未提供自定义翻译器，则默认使用SQLExceptionTranslator
     * @param exceptionTranslator
     */
    public void setExceptionTranslator(SQLExceptionTranslator exceptionTranslator) {
        this.exceptionTranslator = exceptionTranslator;
    }

    /**
     *
     * @return 返回实例的异常转换器
     */
    public SQLExceptionTranslator getExceptionTranslator() {
        SQLExceptionTranslator exceptionTranslator = this.exceptionTranslator;
        if (exceptionTranslator != null) {
            return exceptionTranslator;
        }
        synchronized (this) {
            exceptionTranslator = this.exceptionTranslator;
            if (exceptionTranslator == null) {
                DataSource dataSource = getDataSource();
                if (dataSource != null) {
                    exceptionTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
                }
                else {
                    exceptionTranslator = new SQLStateSQLExceptionTranslator();
                }
                this.exceptionTranslator = exceptionTranslator;
            }
            return exceptionTranslator;
        }
    }

    /**
     * 设置是否对此访问器延迟初始化
     * @param lazyInit
     */
    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    /**
     * 返回是否对此访问器延迟初始化
     * @return
     */
    public boolean isLazyInit() {
        return this.lazyInit;
    }

    /**
     * 实现InitializingBean该方法
     */
    @Override
    public void afterPropertiesSet() {
        if (getDataSource() == null) {
            throw new IllegalArgumentException("Property 'dataSource' is required");
        }
        if (!isLazyInit()) {
            getExceptionTranslator();
        }
    }
}
