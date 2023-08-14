package com.example.y3spring.jdbc.utils;

import com.example.y3spring.jdbc.exception.MetaDataAccessException;
import com.example.y3spring.jdbc.support.DatabaseMetaDataCallback;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;

public class JdbcUtils {

    public static final int TYPE_UNKNOWN = Integer.MIN_VALUE;

    private static final Log logger = LogFactory.getLog(JdbcUtils.class);

    /**
     * 通过DatabaseMetaDataCallback获取数据库元数据
     * @param dataSource
     * @param action
     * @return
     * @throws MetaDataAccessException
     */
    public static Object extractDatabaseMetaData(DataSource dataSource, DatabaseMetaDataCallback action)
            throws MetaDataAccessException {
        // 重置连接
        Connection con = null;
        try {
            // 获取和数据库的连接
            con = DataSourceUtils.getConnection(dataSource);
            DatabaseMetaData metaData = con.getMetaData();
            if (metaData == null) {
                throw new MetaDataAccessException("DatabaseMetaData returned by Connection [" + con + "] was null");
            }
            return action.processMetaData(metaData);
        }
        catch (SQLException ex) {
            throw new MetaDataAccessException("Error while extracting DatabaseMetaData", ex);
        }

    }

    /**
     * 对给定DataSource的DatabaseMetaData调用指定的方法
     * @param dataSource
     * @param metaDataMethodName
     * @return
     * @param <T>
     * @throws MetaDataAccessException
     */

    @SuppressWarnings("unchecked")
    public static <T> T extractDatabaseMetaData(DataSource dataSource, final String metaDataMethodName)
            throws MetaDataAccessException {

        return (T) extractDatabaseMetaData(dataSource,
                dbmd -> {
                    try {
                        return DatabaseMetaData.class.getMethod(metaDataMethodName).invoke(dbmd);
                    }
                    catch (NoSuchMethodException ex) {
                        throw new MetaDataAccessException("No method named '" + metaDataMethodName +
                                "' found on DatabaseMetaData instance [" + dbmd + "]", ex);
                    }
                    catch (IllegalAccessException ex) {
                        throw new MetaDataAccessException(
                                "Could not access DatabaseMetaData method '" + metaDataMethodName + "'", ex);
                    }
                    catch (InvocationTargetException ex) {
                        if (ex.getTargetException() instanceof SQLException) {
                            throw (SQLException) ex.getTargetException();
                        }
                        throw new MetaDataAccessException(
                                "Invocation of DatabaseMetaData method '" + metaDataMethodName + "' failed", ex);
                    }
                });
    }


}
