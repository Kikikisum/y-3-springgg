package com.example.y3spring.transaction.exception;


import org.springframework.core.NestedRuntimeException;

public class TransactionException extends NestedRuntimeException {

    public TransactionException(String msg) {
        super(msg);
    }

    public TransactionException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
