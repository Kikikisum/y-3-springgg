package com.example.y3spring.transaction.dataSource;

import com.example.y3spring.transaction.TransactionDefinition;
import com.example.y3spring.transaction.exception.TransactionException;
import com.example.y3spring.transaction.support.AbstractPlatformTransactionManager;
import com.example.y3spring.transaction.support.DefaultTransactionStatus;
import com.example.y3spring.transaction.support.TransactionSynchronizationManager;
import com.example.y3spring.transaction.support.TransactionSynchronizationUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.lang.Nullable;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DataSourceTransactionManager extends AbstractPlatformTransactionManager implements InitializingBean {

    @Nullable
    private DataSource dataSource;

    private boolean enforceReadOnly = false;


    public DataSourceTransactionManager() {
        setNestedTransactionAllowed(true);
    }

    public DataSourceTransactionManager(DataSource dataSource) {
        this();
        setDataSource(dataSource);
        afterPropertiesSet();
    }

    public void setDataSource(@Nullable DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Nullable
    public DataSource getDataSource() {
        return this.dataSource;
    }

    protected DataSource obtainDataSource() {
        DataSource dataSource = getDataSource();
        return dataSource;
    }

    public void setEnforceReadOnly(boolean enforceReadOnly) {
        this.enforceReadOnly = enforceReadOnly;
    }

    public boolean isEnforceReadOnly() {
        return this.enforceReadOnly;
    }

    @Override
    public void afterPropertiesSet() {
        if (getDataSource() == null) {
            throw new IllegalArgumentException("Property 'dataSource' is required");
        }
    }

    public Object getResourceFactory() {
        return obtainDataSource();
    }

    /**
     * 实际获取
     * @return
     */
    @Override
    protected Object doGetTransaction() {
        DataSourceTransactionObject txObject = new DataSourceTransactionObject();
        txObject.setSavepointAllowed(isNestedTransactionAllowed());
        ConnectionHolder conHolder =
                (ConnectionHolder) TransactionSynchronizationManager.getResource(obtainDataSource());
        txObject.setConnectionHolder(conHolder, false);
        return txObject;
    }

    @Override
    protected boolean isExistingTransaction(Object transaction) {
        DataSourceTransactionObject txObject = (DataSourceTransactionObject) transaction;
        return (txObject.hasConnectionHolder() && txObject.getConnectionHolder().isTransactionActive());
    }

    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {
        DataSourceTransactionObject txObject = (DataSourceTransactionObject) transaction;
        Connection con = null;

        try {
            if (!txObject.hasConnectionHolder() ||
                    txObject.getConnectionHolder().isSynchronizedWithTransaction()) {
                Connection newCon = obtainDataSource().getConnection();
                if (logger.isDebugEnabled()) {
                    logger.debug("Acquired Connection [" + newCon + "] for JDBC transaction");
                }
                txObject.setConnectionHolder(new ConnectionHolder(newCon), true);
            }

            txObject.getConnectionHolder().setSynchronizedWithTransaction(true);
            con = txObject.getConnectionHolder().getConnection();

            Integer previousIsolationLevel = DataSourceUtils.prepareConnectionForTransaction(con, definition);
            txObject.setPreviousIsolationLevel(previousIsolationLevel);

            if (con.getAutoCommit()) {
                txObject.setMustRestoreAutoCommit(true);
                if (logger.isDebugEnabled()) {
                    logger.debug("Switching JDBC Connection [" + con + "] to manual commit");
                }
                con.setAutoCommit(false);
            }

            prepareTransactionalConnection(con, definition);
            txObject.getConnectionHolder().setTransactionActive(true);

            if (txObject.isNewConnectionHolder()) {
                TransactionSynchronizationManager.bindResource(obtainDataSource(), txObject.getConnectionHolder());
            }
        }

        catch (Throwable ex) {
            if (txObject.isNewConnectionHolder()) {
                DataSourceUtils.releaseConnection(con, obtainDataSource());
                txObject.setConnectionHolder(null, false);
            }
        }
    }

    @Override
    protected Object doSuspend(Object transaction) {
        DataSourceTransactionObject txObject = (DataSourceTransactionObject) transaction;
        txObject.setConnectionHolder(null);
        return TransactionSynchronizationManager.unbindResource(obtainDataSource());
    }

    @Override
    protected void doResume(@Nullable Object transaction, Object suspendedResources) {
        TransactionSynchronizationManager.bindResource(obtainDataSource(), suspendedResources);
    }

    @Override
    protected void doCommit(DefaultTransactionStatus status) {
        DataSourceTransactionObject txObject = (DataSourceTransactionObject) status.getTransaction();
        Connection con = txObject.getConnectionHolder().getConnection();
        if (status.isDebug()) {
            logger.debug("Committing JDBC transaction on Connection [" + con + "]");
        }
        try {
            con.commit();
        }
        catch (SQLException ex) {
            throw new TransactionException("Could not commit JDBC transaction", ex);
        }
    }

    @Override
    protected void doRollback(DefaultTransactionStatus status) {
        DataSourceTransactionObject txObject = (DataSourceTransactionObject) status.getTransaction();
        Connection con = txObject.getConnectionHolder().getConnection();
        if (status.isDebug()) {
            logger.debug("Rolling back JDBC transaction on Connection [" + con + "]");
        }
        try {
            con.rollback();
        }
        catch (SQLException ex) {
            throw new TransactionException("Could not roll back JDBC transaction", ex);
        }
    }

    @Override
    protected void doSetRollbackOnly(DefaultTransactionStatus status) {
        DataSourceTransactionObject txObject = (DataSourceTransactionObject) status.getTransaction();
        if (status.isDebug()) {
            logger.debug("Setting JDBC transaction [" + txObject.getConnectionHolder().getConnection() +
                    "] rollback-only");
        }
        txObject.setRollbackOnly();
    }

    @Override
    protected void doCleanupAfterCompletion(Object transaction) {
        DataSourceTransactionObject txObject = (DataSourceTransactionObject) transaction;

        if (txObject.isNewConnectionHolder()) {
            TransactionSynchronizationManager.unbindResource(obtainDataSource());
        }

        Connection con = txObject.getConnectionHolder().getConnection();
        try {
            if (txObject.isMustRestoreAutoCommit()) {
                con.setAutoCommit(true);
            }
            DataSourceUtils.resetConnectionAfterTransaction(con, txObject.getPreviousIsolationLevel());
        }
        catch (Throwable ex) {
            logger.debug("Could not reset JDBC Connection after transaction", ex);
        }

        if (txObject.isNewConnectionHolder()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Releasing JDBC Connection [" + con + "] after transaction");
            }
            DataSourceUtils.releaseConnection(con, this.dataSource);
        }

        txObject.getConnectionHolder().clear();
    }


    protected void prepareTransactionalConnection(Connection con, TransactionDefinition definition)
            throws SQLException {

        if (isEnforceReadOnly() && definition.isReadOnly()) {
            Statement stmt = con.createStatement();
            try {
                stmt.executeUpdate("SET TRANSACTION READ ONLY");
            }
            finally {
                stmt.close();
            }
        }
    }


    private static class DataSourceTransactionObject extends JdbcTransactionObjectSupport {

        private boolean newConnectionHolder;

        private boolean mustRestoreAutoCommit;

        public void setConnectionHolder(@Nullable ConnectionHolder connectionHolder, boolean newConnectionHolder) {
            super.setConnectionHolder(connectionHolder);
            this.newConnectionHolder = newConnectionHolder;
        }

        public boolean isNewConnectionHolder() {
            return this.newConnectionHolder;
        }

        public void setMustRestoreAutoCommit(boolean mustRestoreAutoCommit) {
            this.mustRestoreAutoCommit = mustRestoreAutoCommit;
        }

        public boolean isMustRestoreAutoCommit() {
            return this.mustRestoreAutoCommit;
        }

        public void setRollbackOnly() {
            getConnectionHolder().setRollbackOnly();
        }

        public boolean isRollbackOnly() {
            return getConnectionHolder().isRollbackOnly();
        }

        public void flush() {
            if (TransactionSynchronizationManager.isSynchronizationActive()) {
                TransactionSynchronizationUtils.triggerFlush();
            }
        }
    }

}
