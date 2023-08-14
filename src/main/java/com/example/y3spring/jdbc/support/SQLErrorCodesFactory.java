package com.example.y3spring.jdbc.support;

import com.example.y3spring.beans.factory.co.io.Resource;
import com.example.y3spring.beans.factory.co.io.ResourceLoader;
import com.example.y3spring.beans.factory.support.BeanDefinitionRegistry;
import com.example.y3spring.beans.factory.support.XmlBeanDefinitionReader;
import com.example.y3spring.jdbc.exception.MetaDataAccessException;
import com.example.y3spring.jdbc.utils.JdbcUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.Map;

public class SQLErrorCodesFactory {
    /**
     * 自定义存放SQL错误代码文件的名称(可修改)
     */
    public static final String SQL_ERROR_CODE_OVERRIDE_PATH = "sql-error-codes.xml";

    /**
     * The name of default SQL error code files, loading from the class path.
     */
    public static final String SQL_ERROR_CODE_DEFAULT_PATH = "com/example/y3spring/jdbc/support/sql-error-codes.xml";

    private static final Log logger = LogFactory.getLog(SQLErrorCodesFactory.class);

    private final Map<DataSource, SQLErrorCodes> dataSourceCache = new ConcurrentReferenceHashMap<>(16);

    private final Map<String, SQLErrorCodes> errorCodesMap;

    private static final SQLErrorCodesFactory instance = new SQLErrorCodesFactory();

    public static SQLErrorCodesFactory getInstance() {
        return instance;
    }

    /**
     * 创造工厂
     */
    protected SQLErrorCodesFactory() {
        Map<String, SQLErrorCodes> errorCodes=null;

        try {
            // 还未进行获取
            ResourceLoader resourceLoader = null;
            BeanDefinitionRegistry beanDefinitionRegistry = null;
            XmlBeanDefinitionReader bdr = new XmlBeanDefinitionReader(resourceLoader,beanDefinitionRegistry);

            // 获取错误所在的资源
            Resource resource = ResourceLoader.getResource(SQL_ERROR_CODE_DEFAULT_PATH);
            if (resource != null) {
                //还未实现
            }
            bdr.loadBeanDefinitions(resource);
        }
        catch (BeansException ex) {
            logger.warn("Error loading SQL error codes from config file", ex);
            errorCodes = Collections.emptyMap();
        }
        this.errorCodesMap = errorCodes;
    }


    /**
     * 返回给定数据库的实例
     * @param databaseName
     * @return
     */
    public SQLErrorCodes getErrorCodes(String databaseName) {
        Assert.notNull(databaseName, "Database product name must not be null");

        SQLErrorCodes sec = this.errorCodesMap.get(databaseName);
        if (sec == null) {
            for (SQLErrorCodes candidate : this.errorCodesMap.values()) {
                if (PatternMatchUtils.simpleMatch(candidate.getDatabaseProductNames(), databaseName)) {
                    sec = candidate;
                    break;
                }
            }
        }
        return new SQLErrorCodes();
    }


    public SQLErrorCodes getErrorCodes(DataSource dataSource) {
        Assert.notNull(dataSource, "DataSource must not be null");

        SQLErrorCodes sec = this.dataSourceCache.get(dataSource);
        if (sec == null) {
            synchronized (this.dataSourceCache) {
                sec = this.dataSourceCache.get(dataSource);
                if (sec == null) {
                    try {
                        String name = JdbcUtils.extractDatabaseMetaData(dataSource, "getDatabaseProductName");
                        if (StringUtils.hasLength(name)) {
                            return registerDatabase(dataSource, name);
                        }
                    }
                    catch (MetaDataAccessException ex) {
                        logger.warn("Error while extracting database name - falling back to empty error codes", ex);
                    }
                    return new SQLErrorCodes();
                }
            }
        }


        return sec;
    }

    /**
     * 将指定的数据库名称与给定的DataSource相关联
     * @param dataSource
     * @param databaseName
     * @return
     */
    public SQLErrorCodes registerDatabase(DataSource dataSource, String databaseName) {
        SQLErrorCodes sec = getErrorCodes(databaseName);
        if (logger.isDebugEnabled()) {
            logger.debug("Caching SQL error codes for DataSource [" + identify(dataSource) +
                    "]: database product name is '" + databaseName + "'");
        }
        this.dataSourceCache.put(dataSource, sec);
        return sec;
    }

    @Nullable
    public SQLErrorCodes unregisterDatabase(DataSource dataSource) {
        return this.dataSourceCache.remove(dataSource);
    }

    private String identify(DataSource dataSource) {
        return dataSource.getClass().getName() + '@' + Integer.toHexString(dataSource.hashCode());
    }

}
