package com.example.y3spring.transaction.support;

import com.example.y3spring.transaction.exception.TransactionTimedOutException;
import org.springframework.lang.Nullable;

import java.util.Date;

/**
 * ConnectionHolder的基类
 */
public abstract class ResourceHolderSupport implements ResourceHolder {

    private boolean synchronizedWithTransaction = false;

    private boolean rollbackOnly = false;

    @Nullable
    private Date deadline;

    private int referenceCount = 0;

    private boolean isVoid = false;


    public void setSynchronizedWithTransaction(boolean synchronizedWithTransaction) {
        this.synchronizedWithTransaction = synchronizedWithTransaction;
    }

    public boolean isSynchronizedWithTransaction() {
        return this.synchronizedWithTransaction;
    }

    public void setRollbackOnly() {
        this.rollbackOnly = true;
    }

    public void resetRollbackOnly() {
        this.rollbackOnly = false;
    }

    public boolean isRollbackOnly() {
        return this.rollbackOnly;
    }

    public void setTimeoutInSeconds(int seconds) {
        setTimeoutInMillis(seconds * 1000L);
    }

    public void setTimeoutInMillis(long millis) {
        this.deadline = new Date(System.currentTimeMillis() + millis);
    }

    public boolean hasTimeout() {
        return (this.deadline != null);
    }

    @Nullable
    public Date getDeadline() {
        return this.deadline;
    }

    public int getTimeToLiveInSeconds() {
        double diff = ((double) getTimeToLiveInMillis()) / 1000;
        int secs = (int) Math.ceil(diff);
        checkTransactionTimeout(secs <= 0);
        return secs;
    }

    public long getTimeToLiveInMillis() throws TransactionTimedOutException{
        if (this.deadline == null) {
            throw new IllegalStateException("No timeout specified for this resource holder");
        }
        long timeToLive = this.deadline.getTime() - System.currentTimeMillis();
        checkTransactionTimeout(timeToLive <= 0);
        return timeToLive;
    }

    private void checkTransactionTimeout(boolean deadlineReached) throws TransactionTimedOutException {
        if (deadlineReached) {
            setRollbackOnly();
            throw new TransactionTimedOutException("Transaction timed out: deadline was " + this.deadline);
        }
    }

    /**
     * 对连接进行引用计数（加）
     */
    public void requested() {
        this.referenceCount++;
    }

    /**
     * 对连接进行引用计数（减/释放）
     */
    public void released() {
        this.referenceCount--;
    }

    /**
     * 是否有连接
     * @return
     */
    public boolean isOpen() {
        return (this.referenceCount > 0);
    }

    /**
     * 清除数据
     */
    public void clear() {
        this.synchronizedWithTransaction = false;
        this.rollbackOnly = false;
        this.deadline = null;
    }

    /**
     * 重置连接
     */
    @Override
    public void reset() {
        clear();
        this.referenceCount = 0;
    }

    @Override
    public void unbound() {
        this.isVoid = true;
    }

    @Override
    public boolean isVoid() {
        return this.isVoid;
    }

}