package com.example.y3spring.transaction.support;

import com.example.y3spring.transaction.PlatformTransactionManager;
import com.example.y3spring.transaction.TransactionDefinition;
import com.example.y3spring.transaction.TransactionStatus;
import com.example.y3spring.transaction.exception.IllegalTransactionStateException;
import com.example.y3spring.transaction.exception.TransactionException;
import com.example.y3spring.transaction.exception.TransactionSuspensionNotSupportedException;
import com.example.y3spring.transaction.exception.UnexpectedRollbackException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.Constants;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.List;

public abstract class AbstractPlatformTransactionManager implements PlatformTransactionManager, Serializable {

    public static final int SYNCHRONIZATION_ALWAYS = 0;

    public static final int SYNCHRONIZATION_ON_ACTUAL_TRANSACTION = 1;

    public static final int SYNCHRONIZATION_NEVER = 2;

    private static final Constants constants = new Constants(AbstractPlatformTransactionManager.class);

    protected transient Log logger = LogFactory.getLog(getClass());

    private int transactionSynchronization = SYNCHRONIZATION_ALWAYS;

    private int defaultTimeout = TransactionDefinition.TIMEOUT_DEFAULT;

    private boolean nestedTransactionAllowed = false;

    private boolean validateExistingTransaction = false;

    private boolean globalRollbackOnParticipationFailure = true;

    private boolean failEarlyOnGlobalRollbackOnly = false;

    private boolean rollbackOnCommitFailure = false;


    /**
     * 设置事务同步器的名称
     * @param constantName
     */
    public final void setTransactionSynchronizationName(String constantName) {
        setTransactionSynchronization(constants.asNumber(constantName).intValue());
    }

    public final void setTransactionSynchronization(int transactionSynchronization) {
        this.transactionSynchronization = transactionSynchronization;
    }

    public final int getTransactionSynchronization() {
        return this.transactionSynchronization;
    }


    /**
     * 设置超时时间
     * @param defaultTimeout
     */
    public final void setDefaultTimeout(int defaultTimeout) {
        if (defaultTimeout < TransactionDefinition.TIMEOUT_DEFAULT) {
            throw new RuntimeException("不能设置小于-1的超时时间!");
        }
        this.defaultTimeout = defaultTimeout;
    }

    public final int getDefaultTimeout() {
        return this.defaultTimeout;
    }

    public final void setNestedTransactionAllowed(boolean nestedTransactionAllowed) {
        this.nestedTransactionAllowed = nestedTransactionAllowed;
    }

    public final boolean isNestedTransactionAllowed() {
        return this.nestedTransactionAllowed;
    }

    public final void setValidateExistingTransaction(boolean validateExistingTransaction) {
        this.validateExistingTransaction = validateExistingTransaction;
    }

    public final boolean isValidateExistingTransaction() {
        return this.validateExistingTransaction;
    }

    public final void setGlobalRollbackOnParticipationFailure(boolean globalRollbackOnParticipationFailure) {
        this.globalRollbackOnParticipationFailure = globalRollbackOnParticipationFailure;
    }

    public final boolean isGlobalRollbackOnParticipationFailure() {
        return this.globalRollbackOnParticipationFailure;
    }

    public final void setFailEarlyOnGlobalRollbackOnly(boolean failEarlyOnGlobalRollbackOnly) {
        this.failEarlyOnGlobalRollbackOnly = failEarlyOnGlobalRollbackOnly;
    }

    public final boolean isFailEarlyOnGlobalRollbackOnly() {
        return this.failEarlyOnGlobalRollbackOnly;
    }

    public final void setRollbackOnCommitFailure(boolean rollbackOnCommitFailure) {
        this.rollbackOnCommitFailure = rollbackOnCommitFailure;
    }

    public final boolean isRollbackOnCommitFailure() {
        return this.rollbackOnCommitFailure;
    }


    /**
     * This implementation handles propagation behavior. Delegates to
     * {@code doGetTransaction}, {@code isExistingTransaction}
     * and {@code doBegin}.
     * @see #doGetTransaction
     * @see #isExistingTransaction
     * @see #doBegin
     */
    @Override
    public final TransactionStatus getTransaction(@Nullable TransactionDefinition definition) throws TransactionException {
        // 封装transaction对象并获取当前事务的ConnectionHolder
        Object transaction = doGetTransaction();
        boolean debugEnabled = logger.isDebugEnabled();

        // 当前存在事务，根据事务传播级别处理
        if (isExistingTransaction(transaction)) {
            return handleExistingTransaction(definition, transaction, debugEnabled);
        }

        // 对超时事务进行处理
        if (definition.getTimeout() < TransactionDefinition.TIMEOUT_DEFAULT) {
            throw new RuntimeException("超时啦！！！");
        }
        // 当前不存在事务，根据不同事务传播级别处理
        // 如果传播级别是PROPAGATION_MANDATORY，不存在事务报错
        if (definition.getPropagationBehavior() == TransactionDefinition.PROPAGATION_MANDATORY) {
            throw new RuntimeException("该类型只能在一个事务中！");
        }
        // 如果传播级别是PROPAGATION_REQUIRED、PROPAGATION_REQUIRES_NEW、PROPAGATION_NESTED
        else if (definition.getPropagationBehavior() == TransactionDefinition.PROPAGATION_REQUIRED ||
                definition.getPropagationBehavior() == TransactionDefinition.PROPAGATION_REQUIRES_NEW ||
                definition.getPropagationBehavior() == TransactionDefinition.PROPAGATION_NESTED) {
            // 中断事务同步管理器中注册的同步对象，封装中断事务对象
            SuspendedResourcesHolder suspendedResources = suspend(null);
            try {
                // 开启新事务
                boolean newSynchronization = (getTransactionSynchronization() != SYNCHRONIZATION_NEVER);
                DefaultTransactionStatus status = newTransactionStatus(
                        definition, transaction, true, newSynchronization, debugEnabled, suspendedResources);
                doBegin(transaction, definition);
                prepareSynchronization(status, definition);
                return status;
            }
            catch (RuntimeException | Error ex) {
                // 开启新事务失败，重启中断事务对象中的同步对象
                resume(null, suspendedResources);
                throw ex;
            }
        }
        else {
            // 其他传播级别，以非事务执行代码，但还是封装TransactionStatus返回
            boolean newSynchronization = (getTransactionSynchronization() == SYNCHRONIZATION_ALWAYS);
            return prepareTransactionStatus(definition, null, true, newSynchronization, debugEnabled, null);
        }
    }

    /**
     * 为一个已经存在的事务创造一个TransactionStatus
     */
    private TransactionStatus handleExistingTransaction(
            TransactionDefinition definition, Object transaction, boolean debugEnabled)
            throws TransactionException {

        // 传播级别为PROPAGATION_NOT_SUPPORTED，中断当前事务，以非事务执行
        if (definition.getPropagationBehavior() == TransactionDefinition.PROPAGATION_NOT_SUPPORTED) {
            if (debugEnabled) {
                logger.debug("Suspending current transaction");
            }
            Object suspendedResources = suspend(transaction);
            boolean newSynchronization = (getTransactionSynchronization() == SYNCHRONIZATION_ALWAYS);
            return prepareTransactionStatus(
                    definition, null, false, newSynchronization, debugEnabled, suspendedResources);
        }

        // 传播级别为PROPAGATION_REQUIRES_NEW，中断当前事务，开启新事务
        if (definition.getPropagationBehavior() == TransactionDefinition.PROPAGATION_REQUIRES_NEW) {
            if (debugEnabled) {
                logger.debug("Suspending current transaction, creating new transaction with name [" +
                        definition.getName() + "]");
            }
            SuspendedResourcesHolder suspendedResources = suspend(transaction);
            try {
                return startTransaction(definition, transaction, debugEnabled, suspendedResources);
            }
            catch (RuntimeException | Error beginEx) {
                resumeAfterBeginException(transaction, suspendedResources, beginEx);
                throw beginEx;
            }
        }

        // 传播级别为PROPAGATION_NESTED，jdbc处理为在当前事务上创建一个保存点
        if (definition.getPropagationBehavior() == TransactionDefinition.PROPAGATION_NESTED) {
            if (!isNestedTransactionAllowed()) {
                throw new RuntimeException("事务器不允许嵌套");
            }
            if (useSavepointForNestedTransaction()) {
                DefaultTransactionStatus status =
                        prepareTransactionStatus(definition, transaction, false, false, debugEnabled, null);
                status.createAndHoldSavepoint();
                return status;
            }
            else {
                // JTA事务的处理
                return startTransaction(definition, transaction, debugEnabled, null);
            }
        }

        // 传播级别为PROPAGATION_SUPPORTS、PROPAGATION_REQUIRED以当前事务执行
        if (isValidateExistingTransaction()) {
            if (definition.getIsolationLevel() != TransactionDefinition.ISOLATION_DEFAULT) {
                Integer currentIsolationLevel = TransactionSynchronizationManager.getCurrentTransactionIsolationLevel();
                if (currentIsolationLevel == null || currentIsolationLevel != definition.getIsolationLevel()) {
                    Constants isoConstants = DefaultTransactionDefinition.constants;
                }
            }
            if (!definition.isReadOnly()) {
                if (TransactionSynchronizationManager.isCurrentTransactionReadOnly()) {
                    throw new RuntimeException("只允许读！");
                }
            }
        }
        boolean newSynchronization = (getTransactionSynchronization() != SYNCHRONIZATION_NEVER);
        return prepareTransactionStatus(definition, transaction, false, newSynchronization, debugEnabled, null);
    }

    private TransactionStatus startTransaction(TransactionDefinition definition, Object transaction, boolean debugEnabled, SuspendedResourcesHolder suspendedResources) {
        boolean newSynchronization = (getTransactionSynchronization() != SYNCHRONIZATION_NEVER);
        DefaultTransactionStatus status = newTransactionStatus(
                definition, transaction, true, newSynchronization, debugEnabled, suspendedResources);
        doBegin(transaction, definition);
        prepareSynchronization(status, definition);
        return status;
    }

    /**
     * 为参数创造一个新的TransactionStatus
     * @param definition
     * @param transaction
     * @param newTransaction
     * @param newSynchronization
     * @param debug
     * @param suspendedResources
     * @return
     */
    protected final DefaultTransactionStatus prepareTransactionStatus(
            TransactionDefinition definition, @Nullable Object transaction, boolean newTransaction,
            boolean newSynchronization, boolean debug, @Nullable Object suspendedResources) {

        DefaultTransactionStatus status = newTransactionStatus(
                definition, transaction, newTransaction, newSynchronization, debug, suspendedResources);
        prepareSynchronization(status, definition);
        return status;
    }

    protected DefaultTransactionStatus newTransactionStatus(
            TransactionDefinition definition, @Nullable Object transaction, boolean newTransaction,
            boolean newSynchronization, boolean debug, @Nullable Object suspendedResources) {

        boolean actualNewSynchronization = newSynchronization &&
                !TransactionSynchronizationManager.isSynchronizationActive();
        return new DefaultTransactionStatus(
                transaction, newTransaction, actualNewSynchronization,
                definition.isReadOnly(), debug, suspendedResources);
    }

    /**
     * 初始化事务同步器
     * @param status
     * @param definition
     */
    protected void prepareSynchronization(DefaultTransactionStatus status, TransactionDefinition definition) {
        if (status.isNewSynchronization()) {
            TransactionSynchronizationManager.setActualTransactionActive(status.hasTransaction());
            TransactionSynchronizationManager.setCurrentTransactionIsolationLevel(
                    definition.getIsolationLevel() != TransactionDefinition.ISOLATION_DEFAULT ?
                            definition.getIsolationLevel() : null);
            TransactionSynchronizationManager.setCurrentTransactionReadOnly(definition.isReadOnly());
            TransactionSynchronizationManager.setCurrentTransactionName(definition.getName());
            TransactionSynchronizationManager.initSynchronization();
        }
    }


    /**
     * 挂起事务，需要暂停事务同步
     * @param transaction
     * @return
     * @throws TransactionException
     */
    @Nullable
    protected final SuspendedResourcesHolder suspend(@Nullable Object transaction) throws TransactionException {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            List<TransactionSynchronization> suspendedSynchronizations = doSuspendSynchronization();
            try {
                Object suspendedResources = null;
                if (transaction != null) {
                    suspendedResources = doSuspend(transaction);
                }
                String name = TransactionSynchronizationManager.getCurrentTransactionName();
                TransactionSynchronizationManager.setCurrentTransactionName(null);
                boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
                TransactionSynchronizationManager.setCurrentTransactionReadOnly(false);
                Integer isolationLevel = TransactionSynchronizationManager.getCurrentTransactionIsolationLevel();
                TransactionSynchronizationManager.setCurrentTransactionIsolationLevel(null);
                boolean wasActive = TransactionSynchronizationManager.isActualTransactionActive();
                TransactionSynchronizationManager.setActualTransactionActive(false);
                return new SuspendedResourcesHolder(
                        suspendedResources, suspendedSynchronizations, name, readOnly, isolationLevel, wasActive);
            }
            catch (RuntimeException | Error ex) {
                doResumeSynchronization(suspendedSynchronizations);
                throw ex;
            }
        }
        else if (transaction != null) {
            // 进行重新同步
            Object suspendedResources = doSuspend(transaction);
            return new SuspendedResourcesHolder(suspendedResources);
        }
        else {
            // Neither transaction nor synchronization active.
            return null;
        }
    }

    /**
     * 恢复所暂停的事务
     * @param transaction
     * @param resourcesHolder
     * @throws TransactionException
     */
    protected final void resume(@Nullable Object transaction, @Nullable SuspendedResourcesHolder resourcesHolder)
            throws TransactionException {

        if (resourcesHolder != null) {
            Object suspendedResources = resourcesHolder.suspendedResources;
            if (suspendedResources != null) {
                doResume(transaction, suspendedResources);
            }
            List<TransactionSynchronization> suspendedSynchronizations = resourcesHolder.suspendedSynchronizations;
            if (suspendedSynchronizations != null) {
                TransactionSynchronizationManager.setActualTransactionActive(resourcesHolder.wasActive);
                TransactionSynchronizationManager.setCurrentTransactionIsolationLevel(resourcesHolder.isolationLevel);
                TransactionSynchronizationManager.setCurrentTransactionReadOnly(resourcesHolder.readOnly);
                TransactionSynchronizationManager.setCurrentTransactionName(resourcesHolder.name);
                doResumeSynchronization(suspendedSynchronizations);
            }
        }
    }

    /**
     * 恢复外部事务
     * @param transaction
     * @param suspendedResources
     * @param beginEx
     */
    private void resumeAfterBeginException(
            Object transaction, @Nullable SuspendedResourcesHolder suspendedResources, Throwable beginEx) {

        String exMessage = "Inner transaction begin exception overridden by outer transaction resume exception";
        try {
            resume(transaction, suspendedResources);
        }
        catch (RuntimeException | Error resumeEx) {
            logger.error(exMessage, beginEx);
            throw resumeEx;
        }
    }

    /**
     * 挂起所有的同步事务
     * @return
     */
    private List<TransactionSynchronization> doSuspendSynchronization() {
        List<TransactionSynchronization> suspendedSynchronizations =
                TransactionSynchronizationManager.getSynchronizations();
        for (TransactionSynchronization synchronization : suspendedSynchronizations) {
            synchronization.suspend();
        }
        TransactionSynchronizationManager.clearSynchronization();
        return suspendedSynchronizations;
    }

    /**
     * 重新激活当前线程对的事务同步
     * @param suspendedSynchronizations
     */
    private void doResumeSynchronization(List<TransactionSynchronization> suspendedSynchronizations) {
        TransactionSynchronizationManager.initSynchronization();
        for (TransactionSynchronization synchronization : suspendedSynchronizations) {
            synchronization.resume();
            TransactionSynchronizationManager.registerSynchronization(synchronization);
        }
    }


    /**
     * 提交操作
     * @param status
     * @throws TransactionException
     */
    @Override
    public final void commit(TransactionStatus status) throws TransactionException {

        // TransactionStatus设置了回滚，执行回滚操作
        DefaultTransactionStatus defStatus = (DefaultTransactionStatus) status;
        if (defStatus.isLocalRollbackOnly()) {
            if (defStatus.isDebug()) {
                logger.debug("Transactional code has requested rollback");
            }
            processRollback(defStatus, false);
            return;
        }

        // TransactionStatus的ConnectionHolder设置了回滚，执行回滚操作
        if (!shouldCommitOnGlobalRollbackOnly() && defStatus.isGlobalRollbackOnly()) {
            if (defStatus.isDebug()) {
                logger.debug("Global transaction is marked as rollback-only but transactional code requested commit");
            }
            processRollback(defStatus, true);
            return;
        }
        // 执行提交操作
        processCommit(defStatus);
    }

    /**
     * 实际的提交操作
     * @param status
     * @throws TransactionException
     */
    private void processCommit(DefaultTransactionStatus status) throws TransactionException {
        try {
            boolean beforeCompletionInvoked = false;

            try {
                boolean unexpectedRollback = false;
                // 触发TransactionSynchronization的beforeCommit和beforeCompletion
                triggerBeforeCommit(status);
                triggerBeforeCompletion(status);
                beforeCompletionInvoked = true;

                // 有保存点，即嵌套事务，释放保存点
                if (status.hasSavepoint()) {
                    if (status.isDebug()) {
                        logger.debug("Releasing transaction savepoint");
                    }
                    unexpectedRollback = status.isGlobalRollbackOnly();
                    status.releaseHeldSavepoint();
                }
                // 如果是事务最外层，提交事务
                else if (status.isNewTransaction()) {
                    if (status.isDebug()) {
                        logger.debug("Initiating transaction commit");
                    }
                    unexpectedRollback = status.isGlobalRollbackOnly();
                    doCommit(status);
                }
                else if (isFailEarlyOnGlobalRollbackOnly()) {
                    unexpectedRollback = status.isGlobalRollbackOnly();
                }
            }
            catch (TransactionException ex) {
                // 触发TransactionSynchronization的afterCompletion
                triggerAfterCompletion(status, TransactionSynchronization.STATUS_UNKNOWN);
                throw ex;
            }
            catch (RuntimeException | Error ex) {
                if (!beforeCompletionInvoked) {
                    triggerBeforeCompletion(status);
                }
                // 回滚事务
                doRollbackOnCommitException(status, ex);
                throw ex;
            }

            // 触发TransactionSynchronization的afterCommit和afterCompletion
            try {
                triggerAfterCommit(status);
            }
            finally {
                triggerAfterCompletion(status, TransactionSynchronization.STATUS_COMMITTED);
            }

        }
        finally {
            // 有中断事务恢复中断事务，没有重置TransactionSynchronizationManager
            cleanupAfterCompletion(status);
        }
    }

    /**
     * 回滚事务
     * @param status
     * @throws TransactionException
     */
    @Override
    public final void rollback(TransactionStatus status) throws TransactionException {
        if (status.isCompleted()) {
            throw new RuntimeException("该事务已完成！");
        }

        DefaultTransactionStatus defStatus = (DefaultTransactionStatus) status;
        processRollback(defStatus, false);
    }

    /**
     * 进行回滚事务的实际操作
     */
    private void processRollback(DefaultTransactionStatus status, boolean unexpected) {
        try {
            boolean unexpectedRollback = unexpected;

            try {
                // 触发TransactionSynchronization的beforeCompletion
                triggerBeforeCompletion(status);

                // 有保存点，即嵌套事务，回滚到保存点
                if (status.hasSavepoint()) {
                    if (status.isDebug()) {
                        logger.debug("Rolling back transaction to savepoint");
                    }
                    status.rollbackToHeldSavepoint();
                }
                // 如果是事务最外层，回滚事务
                else if (status.isNewTransaction()) {
                    if (status.isDebug()) {
                        logger.debug("Initiating transaction rollback");
                    }
                    doRollback(status);
                }
                else {
                    // 如果事务标记为回滚，将ConnectionHolder标记为回滚
                    if (status.hasTransaction()) {
                        if (status.isLocalRollbackOnly() || isGlobalRollbackOnParticipationFailure()) {
                            if (status.isDebug()) {
                                logger.debug("Participating transaction failed - marking existing transaction as rollback-only");
                            }
                            doSetRollbackOnly(status);
                        }
                    }
                    else {
                        logger.debug("Should roll back transaction but cannot - no transaction available");
                    }
                    if (!isFailEarlyOnGlobalRollbackOnly()) {
                        unexpectedRollback = false;
                    }
                }
            }
            catch (RuntimeException | Error ex) {
                triggerAfterCompletion(status, TransactionSynchronization.STATUS_UNKNOWN);
                throw ex;
            }

            triggerAfterCompletion(status, TransactionSynchronization.STATUS_ROLLED_BACK);

            if (unexpectedRollback) {
                throw new UnexpectedRollbackException(
                        "Transaction rolled back because it has been marked as rollback-only");
            }
        }
        finally {
            //有中断事务恢复中断事务，没有重置TransactionSynchronizationManager
            cleanupAfterCompletion(status);
        }
    }

    /**
     * 正确处理回滚异常
     * @param status
     * @param ex
     * @throws TransactionException
     */
    private void doRollbackOnCommitException(DefaultTransactionStatus status, Throwable ex) throws TransactionException {
        try {
            if (status.isNewTransaction()) {
                if (status.isDebug()) {
                    logger.debug("Initiating transaction rollback after commit exception", ex);
                }
                doRollback(status);
            }
            else if (status.hasTransaction() && isGlobalRollbackOnParticipationFailure()) {
                if (status.isDebug()) {
                    logger.debug("Marking existing transaction as rollback-only after commit exception", ex);
                }
                doSetRollbackOnly(status);
            }
        }
        catch (RuntimeException | Error rbex) {
            logger.error("Commit exception overridden by rollback exception", ex);
            triggerAfterCompletion(status, TransactionSynchronization.STATUS_UNKNOWN);
            throw rbex;
        }
        triggerAfterCompletion(status, TransactionSynchronization.STATUS_ROLLED_BACK);
    }


    /**
     * 触发beforeCommit回调
     * @param status
     */
    protected final void triggerBeforeCommit(DefaultTransactionStatus status) {
        if (status.isNewSynchronization()) {
            if (status.isDebug()) {
                logger.trace("Triggering beforeCommit synchronization");
            }
            TransactionSynchronizationUtils.triggerBeforeCommit(status.isReadOnly());
        }
    }

    /**
     * 触发beforeCompletion回调
     * @param status
     */
    protected final void triggerBeforeCompletion(DefaultTransactionStatus status) {
        if (status.isNewSynchronization()) {
            if (status.isDebug()) {
                logger.trace("Triggering beforeCompletion synchronization");
            }
            TransactionSynchronizationUtils.triggerBeforeCompletion();
        }
    }

    /**
     * 触发afterCommit回调
     */
    private void triggerAfterCommit(DefaultTransactionStatus status) {
        if (status.isNewSynchronization()) {
            if (status.isDebug()) {
                logger.trace("Triggering afterCommit synchronization");
            }
            TransactionSynchronizationUtils.triggerAfterCommit();
        }
    }

    private void triggerAfterCompletion(DefaultTransactionStatus status, int completionStatus) {
        if (status.isNewSynchronization()) {
            List<TransactionSynchronization> synchronizations = TransactionSynchronizationManager.getSynchronizations();
            TransactionSynchronizationManager.clearSynchronization();
            if (!status.hasTransaction() || status.isNewTransaction()) {
                if (status.isDebug()) {
                    logger.trace("Triggering afterCompletion synchronization");
                }

                invokeAfterCompletion(synchronizations, completionStatus);
            }
            else if (!synchronizations.isEmpty()) {
                registerAfterCompletionWithExistingTransaction(status.getTransaction(), synchronizations);
            }
        }
    }

    /**
     * 实际调用给定的TransactionSynchronization对象
     * @param synchronizations
     * @param completionStatus
     */
    protected final void invokeAfterCompletion(List<TransactionSynchronization> synchronizations, int completionStatus) {
        TransactionSynchronizationUtils.invokeAfterCompletion(synchronizations, completionStatus);
    }

    /**
     * 事务完成后进行清理，必要时清除同步
     * @param status
     */
    private void cleanupAfterCompletion(DefaultTransactionStatus status) {
        status.setCompleted();
        if (status.isNewSynchronization()) {
            TransactionSynchronizationManager.clear();
        }
        if (status.isNewTransaction()) {
            doCleanupAfterCompletion(status.getTransaction());
        }
        if (status.getSuspendedResources() != null) {
            if (status.isDebug()) {
                logger.debug("Resuming suspended transaction after completion of inner transaction");
            }
            Object transaction = (status.hasTransaction() ? status.getTransaction() : null);
            resume(transaction, (SuspendedResourcesHolder) status.getSuspendedResources());
        }
    }


    /**
     * 获取Transaction的实际操作
     * @return
     * @throws TransactionException
     */
    protected abstract Object doGetTransaction() throws TransactionException;

    protected boolean isExistingTransaction(Object transaction) throws TransactionException {
        return false;
    }

    protected boolean useSavepointForNestedTransaction() {
        return true;
    }

    /**
     * 开始一个新事务
     * @param transaction
     * @param definition
     * @throws TransactionException
     */
    protected abstract void doBegin(Object transaction, TransactionDefinition definition)
            throws TransactionException;

    /**
     * 挂起当前事务的资源，事务同步将已挂起
     * @param transaction
     * @return
     * @throws TransactionException
     */
    protected Object doSuspend(Object transaction) throws TransactionException {
        throw new TransactionSuspensionNotSupportedException(
                "Transaction manager [" + getClass().getName() + "] does not support transaction suspension");
    }

    /**
     * 恢复当前事务的资源，之后将恢复事务同步
     * @param transaction
     * @param suspendedResources
     * @throws TransactionException
     */
    protected void doResume(@Nullable Object transaction, Object suspendedResources) throws TransactionException {
        throw new TransactionSuspensionNotSupportedException(
                "Transaction manager [" + getClass().getName() + "] does not support transaction suspension");
    }

    protected boolean shouldCommitOnGlobalRollbackOnly() {
        return false;
    }



    /**
     * 实际的提交操作
     * @param status
     * @throws TransactionException
     */
    protected abstract void doCommit(DefaultTransactionStatus status) throws TransactionException;

    /**
     * 实际的回滚操作
     * @param status
     * @throws TransactionException
     */
    protected abstract void doRollback(DefaultTransactionStatus status) throws TransactionException;

    /**
     * 仅设置给定的事务回滚，仅在回滚时调用
     * @param status
     * @throws TransactionException
     */
    protected void doSetRollbackOnly(DefaultTransactionStatus status) throws TransactionException {
        throw new IllegalTransactionStateException(
                "Participating in existing transactions is not supported - when 'isExistingTransaction' " +
                        "returns true, appropriate 'doSetRollbackOnly' behavior must be provided");
    }

    /**
     * 将给定的事务同步列表注册到现有事务
     * @param transaction
     * @param synchronizations
     * @throws TransactionException
     */
    protected void registerAfterCompletionWithExistingTransaction(
            Object transaction, List<TransactionSynchronization> synchronizations) throws TransactionException {

        logger.debug("Cannot register Spring after-completion synchronization with existing transaction - " +
                "processing Spring after-completion callbacks immediately, with outcome status 'unknown'");
        invokeAfterCompletion(synchronizations, TransactionSynchronization.STATUS_UNKNOWN);
    }

    protected void doCleanupAfterCompletion(Object transaction) {
    }


    /**
     * 暂停资源的持有者
     */
    protected static class SuspendedResourcesHolder {

        @Nullable
        private final Object suspendedResources;

        @Nullable
        private List<TransactionSynchronization> suspendedSynchronizations;

        @Nullable
        private String name;

        private boolean readOnly;

        @Nullable
        private Integer isolationLevel;

        private boolean wasActive;

        private SuspendedResourcesHolder(Object suspendedResources) {
            this.suspendedResources = suspendedResources;
        }

        private SuspendedResourcesHolder(
                @Nullable Object suspendedResources, List<TransactionSynchronization> suspendedSynchronizations,
                @Nullable String name, boolean readOnly, @Nullable Integer isolationLevel, boolean wasActive) {

            this.suspendedResources = suspendedResources;
            this.suspendedSynchronizations = suspendedSynchronizations;
            this.name = name;
            this.readOnly = readOnly;
            this.isolationLevel = isolationLevel;
            this.wasActive = wasActive;
        }
    }

}
