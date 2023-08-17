package com.example.y3spring.transaction;

import com.example.y3spring.transaction.exception.TransactionException;

public interface SavepointManager {

    /**
     * 创造一个保存点
     * @return
     * @throws TransactionException
     */
    Object createSavepoint() throws TransactionException;

    /**
     * 回滚到保存点
     * @param savepoint
     * @throws TransactionException
     */
    void rollbackToSavepoint(Object savepoint) throws TransactionException;

    /**
     * 释放保存点
     * @param savepoint
     * @throws TransactionException
     */
    void releaseSavepoint(Object savepoint) throws TransactionException;

}
