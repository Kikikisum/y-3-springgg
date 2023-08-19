package com.example.y3spring.transaction.exception;

public class TransactionUsageException extends TransactionException{

    public TransactionUsageException(String msg) {
        super(msg);
    }

    public TransactionUsageException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
