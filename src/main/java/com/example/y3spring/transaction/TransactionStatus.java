package com.example.y3spring.transaction;

/**
 *
 */
public interface TransactionStatus {

    /**
     * 此事务内部是否携带保存点
     * @return
     */
    boolean hasSavepoint();


    /**
     * 是否要进行回滚事务
     * @return
     */
    boolean isRollbackOnly();

    /**
     * 是否完成事务
     * @return
     */
    boolean isCompleted();
}
