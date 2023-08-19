package com.example.y3spring.transaction.DataSource;

import com.example.y3spring.transaction.TransactionDefinition;
import com.example.y3spring.transaction.support.TransactionSynchronizationManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.lang.Nullable;

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
     * 获取连接
     * @param dataSource
     * @return
     * @throws SQLException
     */
    public static Connection doGetConnection(DataSource dataSource) throws SQLException {
        ConnectionHolder conHolder = (ConnectionHolder) TransactionSynchronizationManager.getResource(dataSource);
        if (conHolder != null || conHolder.isSynchronizedWithTransaction()) {
            conHolder.requested();
            return conHolder.getConnection();
        }
        logger.debug("Fetching JDBC Connection from DataSource");
        // 通过dataSource直接获取连接
        Connection con = fetchConnection(dataSource);

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            logger.debug("Registering transaction synchronization for JDBC Connection");

            ConnectionHolder holderToUse = conHolder;
            holderToUse.setConnection(con);
            holderToUse.requested();
            TransactionSynchronizationManager.registerSynchronization(new ConnectionSynchronization(holderToUse, dataSource));
            holderToUse.setSynchronizedWithTransaction(true);
            if (holderToUse != conHolder) {
                TransactionSynchronizationManager.bindResource(dataSource, holderToUse);
            }
        }

        return con;

    }

    /**
     * 获取连接
     * @param dataSource
     * @return
     * @throws SQLException
     */
    private static Connection fetchConnection(DataSource dataSource) throws SQLException {
        Connection con = dataSource.getConnection();
        if (con == null) {
            throw new IllegalStateException("DataSource returned null from getConnection(): " + dataSource);
        }
        return con;
    }

    public static void close(Connection con, PreparedStatement pst) {
    }

    public static Integer prepareConnectionForTransaction(Connection con, TransactionDefinition definition) {
        if (definition != null && definition.isReadOnly()) {
            try {
                if (logger.isDebugEnabled()) {
                    logger.debug("Setting JDBC Connection [" + con + "] read-only");
                }
                con.setReadOnly(true);
            }
            catch (SQLException | RuntimeException ex) {
                try {
                    throw ex;
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        Integer previousIsolationLevel = null;
        if (definition != null && definition.getIsolationLevel() != TransactionDefinition.ISOLATION_DEFAULT) {
            if (logger.isDebugEnabled()) {
                logger.debug("Changing isolation level of JDBC Connection [" + con + "] to " +
                        definition.getIsolationLevel());
            }
            int currentIsolation = 0;
            try {
                currentIsolation = con.getTransactionIsolation();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if (currentIsolation != definition.getIsolationLevel()) {
                previousIsolationLevel = currentIsolation;
                try {
                    con.setTransactionIsolation(definition.getIsolationLevel());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return previousIsolationLevel;
    }

    public static void releaseConnection(Connection con, DataSource dataSource) {
        try {
            doReleaseConnection(con, dataSource);
        }
        catch (SQLException ex) {
            logger.debug("Could not close JDBC Connection", ex);
        }
        catch (Throwable ex) {
            logger.debug("Unexpected exception on closing JDBC Connection", ex);
        }
    }

    public static void doReleaseConnection(@Nullable Connection con, @Nullable DataSource dataSource) throws SQLException {
        if (con == null) {
            return;
        }
        if (dataSource != null) {
            ConnectionHolder conHolder = (ConnectionHolder) TransactionSynchronizationManager.getResource(dataSource);
            if (conHolder != null && connectionEquals(conHolder, con)) {
                conHolder.released();
                return;
            }
        }
        logger.debug("Returning JDBC Connection to DataSource");
        doCloseConnection(con, dataSource);
    }

    public static void doCloseConnection(Connection con, @Nullable DataSource dataSource) throws SQLException {
        if (!(dataSource instanceof SmartDataSource) || ((SmartDataSource) dataSource).shouldClose(con)) {
            con.close();
        }
    }

    private static boolean connectionEquals(ConnectionHolder conHolder, Connection passedInCon) {
        if (!conHolder.hasConnection()) {
            return false;
        }
        Connection heldCon = conHolder.getConnection();

        return (heldCon == passedInCon || heldCon.equals(passedInCon) ||
                getTargetConnection(heldCon).equals(passedInCon));
    }

    public static Connection getTargetConnection(Connection con) {
        Connection conToUse = con;
        while (conToUse instanceof ConnectionProxy) {
            conToUse = ((ConnectionProxy) conToUse).getTargetConnection();
        }
        return conToUse;
    }

    public static void resetConnectionAfterTransaction(Connection con, Integer previousIsolationLevel) {
        try {
            if (previousIsolationLevel != null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Resetting isolation level of JDBC Connection [" +
                            con + "] to " + previousIsolationLevel);
                }
                con.setTransactionIsolation(previousIsolationLevel);
            }

            if (con.isReadOnly()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Resetting read-only flag of JDBC Connection [" + con + "]");
                }
                con.setReadOnly(false);
            }
        }
        catch (Throwable ex) {
            logger.debug("Could not reset JDBC Connection after transaction", ex);
        }
    }
}
