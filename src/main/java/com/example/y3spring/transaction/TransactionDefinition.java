package com.example.y3spring.transaction;

import org.springframework.lang.Nullable;

/**
 * 包含一个事务的定义的属性
 */
public interface TransactionDefinition {

    // 默认
    int PROPAGATION_REQUIRED = 0;
    // 依赖外层，非事务继续运行
    int PROPAGATION_SUPPORTS = 1;
    // 无外层则抛出异常
    int PROPAGATION_MANDATORY = 2;
    // 开启新事务，当前有事务时，挂起处理
    int PROPAGATION_REQUIRES_NEW = 3;
    // 当前有事务时，挂起处理，不回滚
    int PROPAGATION_NOT_SUPPORTED = 4;
    // 外层有事务则抛出异常
    int PROPAGATION_NEVER = 5;
    // 可保存状态保存点
    int PROPAGATION_NESTED = 6;
    // 读已提交
    int ISOLATION_DEFAULT = -1;
    // 读未提交
    int ISOLATION_READ_UNCOMMITTED = 1;
    // 读已提交
    int ISOLATION_READ_COMMITTED = 2;
    // 可重复读
    int ISOLATION_REPEATABLE_READ = 4;
    // 串行化
    int ISOLATION_SERIALIZABLE = 8;
    // 超时
    int TIMEOUT_DEFAULT = -1;

    // 返回事务的传播行为，由是否有一个活动的事务来决定一个事务调用

    default int getPropagationBehavior() {
        return 0;
    }

    // 返回事务的隔离级别，事务管理器依据它来控制另外一个事务能够看到本事务内的哪些数据
    default int getIsolationLevel() {
        return -1;
    }

    // 返回事务必须在多少秒内完毕
    default int getTimeout() {
        return -1;
    }

    // 事务是否仅仅读，事务管理器可以依据这个返回值进行优化。确保事务是仅仅读的
    default boolean isReadOnly() {
        return false;
    }

    @Nullable
    default String getName() {
        return null;
    }



}
