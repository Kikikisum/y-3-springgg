package com.example.y3spring.transaction.support;

import java.io.Flushable;

public interface TransactionSynchronization extends Flushable {

    // 正确提交时的完成状态
    int STATUS_COMMITTED = 0;

    // 在正确回滚的情况下完成状态
    int STATUS_ROLLED_BACK = 1;

    // 在启发式混合完成或系统错误的情况下的完成状态
    int STATUS_UNKNOWN = 2;

    /**
     * 事务挂起时调用,从TransactionSynchronizationManager 取消绑定资源。
     */
    default void suspend() {
    }

    /**
     * 事务恢复时调用,将资源重新绑定到TransactionSynchronizationManager
     */
    default void resume() {
    }

    /**
     * 将底层会话刷新到数据存储区
     */
    @Override
    default void flush() {
    }

    /**
     * 事务提交前调用。此处若发生异常，会导致回滚
     * @param readOnly
     */
    default void beforeCommit(boolean readOnly) {
    }

    /**
     * 事务提交前, 在 beforeCommit 后调用
     */
    default void beforeCompletion() {
    }

    /**
     * 事务提交后调用
     */
    default void afterCommit() {
    }

    /**
     * 事务提交 或回滚后执行
     * @param status
     */
    default void afterCompletion(int status) {
    }
}
