package com.example.y3spring.transaction.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.scope.ScopedObject;
import org.springframework.core.InfrastructureProxy;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.util.List;

public abstract class TransactionSynchronizationUtils {
    private static final Log logger = LogFactory.getLog(TransactionSynchronizationUtils.class);

    private static final boolean aopAvailable = ClassUtils.isPresent(
            "org.springframework.aop.scope.ScopedObject", TransactionSynchronizationUtils.class.getClassLoader());

    /**
     * 必要的时候打开资源
     */
    static Object unwrapResourceIfNecessary(Object resource) {
        Assert.notNull(resource, "资源为空，无法打开");
        Object resourceRef = resource;
        // unwrap infrastructure proxy
        if (resourceRef instanceof InfrastructureProxy) {
            resourceRef = ((InfrastructureProxy) resourceRef).getWrappedObject();
        }
        if (aopAvailable) {
            // now unwrap scoped proxy
            resourceRef = ScopedProxyUnwrapper.unwrapIfNecessary(resourceRef);
        }
        return resourceRef;
    }


    /**
     * 当前注册的同步器进行回调
     */
    public static void triggerFlush() {
        for (TransactionSynchronization synchronization : TransactionSynchronizationManager.getSynchronizations()) {
            synchronization.flush();
        }
    }

    /**
     * 在提交前触发回调
     * @param readOnly
     */
    public static void triggerBeforeCommit(boolean readOnly) {
        for (TransactionSynchronization synchronization : TransactionSynchronizationManager.getSynchronizations()) {
            synchronization.beforeCommit(readOnly);
        }
    }

    /**
     * 在完成事务前触发回调
     */
    public static void triggerBeforeCompletion() {
        for (TransactionSynchronization synchronization : TransactionSynchronizationManager.getSynchronizations()) {
            try {
                synchronization.beforeCompletion();
            }
            catch (Throwable tsex) {
                logger.error("TransactionSynchronization.beforeCompletion threw exception", tsex);
            }
        }
    }

    /**
     * 在提交后触发回调
     */
    public static void triggerAfterCommit() {
        invokeAfterCommit(TransactionSynchronizationManager.getSynchronizations());
    }


    /**
     * 在提交后调用操作
     * @param synchronizations
     */
    public static void invokeAfterCommit(@Nullable List<TransactionSynchronization> synchronizations) {
        if (synchronizations != null) {
            for (TransactionSynchronization synchronization : synchronizations) {
                synchronization.afterCommit();
            }
        }
    }

    /**
     * 完成事务后触发操作
     * @param completionStatus
     */
    public static void triggerAfterCompletion(int completionStatus) {
        List<TransactionSynchronization> synchronizations = TransactionSynchronizationManager.getSynchronizations();
        invokeAfterCompletion(synchronizations, completionStatus);
    }

    /**
     * 在完成后调用操作
     * @param synchronizations
     * @param completionStatus
     */
    public static void invokeAfterCompletion(@Nullable List<TransactionSynchronization> synchronizations,
                                             int completionStatus) {

        if (synchronizations != null) {
            for (TransactionSynchronization synchronization : synchronizations) {
                try {
                    synchronization.afterCompletion(completionStatus);
                }
                catch (Throwable tsex) {
                    logger.error("TransactionSynchronization.afterCompletion threw exception", tsex);
                }
            }
        }
    }


    /**
     * 类中的方法避免对aop模块的硬编码依赖
     */
    private static class ScopedProxyUnwrapper {

        public static Object unwrapIfNecessary(Object resource) {
            if (resource instanceof ScopedObject) {
                return ((ScopedObject) resource).getTargetObject();
            }
            else {
                return resource;
            }
        }
    }
}
