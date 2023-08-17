package com.example.y3spring.transaction;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

public class DefaultTransactionStatus extends AbstractTransactionStatus{
    @Nullable
    private final Object transaction;

    private final boolean newTransaction;

    private final boolean newSynchronization;

    private final boolean readOnly;

    private final boolean debug;

    @Nullable
    private final Object suspendedResources;

    public DefaultTransactionStatus(@Nullable Object transaction, boolean newTransaction,
                                    boolean newSynchronization, boolean readOnly,
                                    boolean debug, @Nullable Object suspendedResources) {

        this.transaction = transaction;
        this.newTransaction = newTransaction;
        this.newSynchronization = newSynchronization;
        this.readOnly = readOnly;
        this.debug = debug;
        this.suspendedResources = suspendedResources;
    }

    public Object getTransaction() {
        return this.transaction;
    }

    public boolean hasTransaction() {
        return (this.transaction != null);
    }

    public boolean isNewSynchronization() {
        return this.newSynchronization;
    }

    public boolean isReadOnly() {
        return this.readOnly;
    }

    public boolean isDebug() {
        return this.debug;
    }

    @Nullable
    public Object getSuspendedResources() {
        return this.suspendedResources;
    }

    public boolean isTransactionSavepointManager() {
        return (this.transaction instanceof SavepointManager);
    }

    @Override
    protected SavepointManager getSavepointManager() {
        Object transaction = this.transaction;
        if (!(transaction instanceof SavepointManager)) {
            throw new RuntimeException("该事务["+this.transaction+"]不支持保存点!");
        }
        return (SavepointManager) transaction;
    }

    public void flush() {
        // 还未实现
    }
}
