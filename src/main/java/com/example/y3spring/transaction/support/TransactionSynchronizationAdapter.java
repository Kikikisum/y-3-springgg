package com.example.y3spring.transaction.support;

import org.springframework.core.Ordered;

public abstract class TransactionSynchronizationAdapter implements TransactionSynchronization, Ordered {
    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    public void suspend() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void flush() {
    }

    @Override
    public void beforeCommit(boolean readOnly) {
    }

    @Override
    public void beforeCompletion() {
    }

    @Override
    public void afterCommit() {
    }

    @Override
    public void afterCompletion(int status) {
    }
}
