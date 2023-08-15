package com.example.y3spring.jdbc.utils;

import com.example.y3spring.jdbc.exception.CannotGetJdbcConnectionException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class DataSourceUtils {

    public static final int CONNECTION_SYNCHRONIZATION_ORDER = 1000;

    private static final Log logger = LogFactory.getLog(DataSourceUtils.class);

    public static Connection getConnection(DataSource dataSource) throws SQLException {
        return doGetConnection(dataSource);
    }

    /**
     *
     * @param dataSource
     * @return
     * @throws SQLException
     */
    public static Connection doGetConnection(DataSource dataSource) throws SQLException {

        return null;

    }

    public static void close(Connection con, PreparedStatement pst) {
    }
}
