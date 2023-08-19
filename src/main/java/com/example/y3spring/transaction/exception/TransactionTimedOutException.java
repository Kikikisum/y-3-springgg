package com.example.y3spring.transaction.exception;

public class TransactionTimedOutException extends TransactionException{
    public TransactionTimedOutException(String msg) {
        super(msg);
    }

    public TransactionTimedOutException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
