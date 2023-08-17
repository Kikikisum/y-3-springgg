package com.example.y3spring.transaction;

public class SimpleTransactionStatus extends AbstractTransactionStatus{
    private final boolean newTransaction;

    public SimpleTransactionStatus() {
        this(true);
    }

    public SimpleTransactionStatus(boolean newTransaction) {
        this.newTransaction = newTransaction;
    }

    public boolean isNewTransaction() {
        return this.newTransaction;
    }
}
