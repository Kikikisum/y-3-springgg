package com.example.y3spring.transaction.dataSource;

import com.example.y3spring.transaction.SavepointManager;
import com.example.y3spring.transaction.exception.TransactionException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.sql.SQLException;
import java.sql.Savepoint;

public class JdbcTransactionObjectSupport implements SavepointManager{


    private static final Log logger = LogFactory.getLog(JdbcTransactionObjectSupport.class);


    @Nullable
    private ConnectionHolder connectionHolder;

    @Nullable
    private Integer previousIsolationLevel;

    private boolean savepointAllowed = false;


    public void setConnectionHolder(@Nullable ConnectionHolder connectionHolder) {
        this.connectionHolder = connectionHolder;
    }

    public ConnectionHolder getConnectionHolder() {
        Assert.state(this.connectionHolder != null, "No ConnectionHolder available");
        return this.connectionHolder;
    }

    public boolean hasConnectionHolder() {
        return (this.connectionHolder != null);
    }

    public void setPreviousIsolationLevel(@Nullable Integer previousIsolationLevel) {
        this.previousIsolationLevel = previousIsolationLevel;
    }

    @Nullable
    public Integer getPreviousIsolationLevel() {
        return this.previousIsolationLevel;
    }

    public void setSavepointAllowed(boolean savepointAllowed) {
        this.savepointAllowed = savepointAllowed;
    }

    public boolean isSavepointAllowed() {
        return this.savepointAllowed;
    }


    /**
     * 创造一个保存点，实际由ConnectionHolder来执行
     * @return
     * @throws TransactionException
     */
    @Override
    public Object createSavepoint() throws TransactionException {
        ConnectionHolder conHolder = getConnectionHolderForSavepoint();
        try {
            return conHolder.createSavepoint();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This implementation rolls back to the given JDBC 3.0 Savepoint.
     * @see java.sql.Connection#rollback(java.sql.Savepoint)
     */
    @Override
    public void rollbackToSavepoint(Object savepoint) throws TransactionException {
        ConnectionHolder conHolder = getConnectionHolderForSavepoint();
        try {
            conHolder.getConnection().rollback((Savepoint) savepoint);
            conHolder.resetRollbackOnly();
        }
        catch (Throwable ex) {
            throw new TransactionException("Could not roll back to JDBC savepoint", ex);
        }
    }

    /**
     * 释放保存点
     * @param savepoint
     * @throws TransactionException
     */
    @Override
    public void releaseSavepoint(Object savepoint) throws TransactionException {
        ConnectionHolder conHolder = getConnectionHolderForSavepoint();
        try {
            conHolder.getConnection().releaseSavepoint((Savepoint) savepoint);
        }
        catch (Throwable ex) {
            logger.debug("Could not explicitly release JDBC savepoint", ex);
        }
    }

    protected ConnectionHolder getConnectionHolderForSavepoint() throws TransactionException {
        return getConnectionHolder();
    }

}
