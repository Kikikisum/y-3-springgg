package com.example.y3spring.transaction.exception;

public class IllegalTransactionStateException extends TransactionException{
    public IllegalTransactionStateException(String msg) {
        super(msg);
    }

    public IllegalTransactionStateException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
