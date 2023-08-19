package com.example.y3spring.transaction.exception;

public class TransactionSuspensionNotSupportedException extends TransactionException{
    public TransactionSuspensionNotSupportedException(String msg) {
        super(msg);
    }

    public TransactionSuspensionNotSupportedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
