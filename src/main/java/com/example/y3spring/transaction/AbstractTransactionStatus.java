package com.example.y3spring.transaction;

import com.example.y3spring.transaction.exception.TransactionException;
import org.springframework.lang.Nullable;

public abstract class AbstractTransactionStatus implements TransactionStatus{

    private boolean rollbackOnly = false;

    private boolean completed = false;

    @Nullable
    private Object savepoint;

    public void setRollbackOnly() {
        this.rollbackOnly = true;
    }

    public boolean isRollbackOnly() {
        return (isLocalRollbackOnly() || isGlobalRollbackOnly());
    }

    public boolean isLocalRollbackOnly() {
        return this.rollbackOnly;
    }

    public boolean isGlobalRollbackOnly() {
        return false;
    }

    public void setCompleted() {
        this.completed = true;
    }

    @Override
    public boolean isCompleted() {
        return this.completed;
    }

    protected void setSavepoint(@Nullable Object savepoint) {
        this.savepoint = savepoint;
    }

    @Nullable
    protected Object getSavepoint() {
        return this.savepoint;
    }

    @Override
    public boolean hasSavepoint() {
        return (this.savepoint != null);
    }


    public Object createSavepoint() throws TransactionException {
        // 还未实现
        return getSavepointManager().createSavepoint();
    }


    public void rollbackToSavepoint(Object savepoint) throws TransactionException {
        // 还未实现
        getSavepointManager().rollbackToSavepoint(savepoint);
    }

    public void releaseSavepoint(Object savepoint) throws TransactionException {
        // 还未实现
        getSavepointManager().releaseSavepoint(savepoint);
    }

    protected SavepointManager getSavepointManager() {
        throw new RuntimeException("该事务不存在保存点!");
    }
}
