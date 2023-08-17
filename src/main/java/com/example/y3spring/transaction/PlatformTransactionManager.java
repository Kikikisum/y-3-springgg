package com.example.y3spring.transaction;

import com.example.y3spring.transaction.exception.TransactionException;
import org.springframework.lang.Nullable;

public interface PlatformTransactionManager {

    //获得当前事务状态
    TransactionStatus getTransaction(@Nullable TransactionDefinition definition) throws TransactionException;

    //提交事务

    void commit(TransactionStatus status) throws TransactionException;

    //回滚事务
    void rollback(TransactionStatus status) throws TransactionException;
}
