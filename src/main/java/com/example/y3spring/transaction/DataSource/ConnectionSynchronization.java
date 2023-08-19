package com.example.y3spring.transaction.DataSource;

import com.example.y3spring.transaction.support.TransactionSynchronizationAdapter;
import com.example.y3spring.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;

import static com.example.y3spring.transaction.DataSource.DataSourceUtils.releaseConnection;

public class ConnectionSynchronization extends TransactionSynchronizationAdapter {
    private final ConnectionHolder connectionHolder;

    private final DataSource dataSource;

    private int order;

    private boolean holderActive = true;

    public ConnectionSynchronization(ConnectionHolder holderToUse, DataSource dataSource) {
        this.connectionHolder=holderToUse;
        this.dataSource=dataSource;
    }


    @Override
    public int getOrder() {
        return this.order;
    }

    @Override
    public void suspend() {
        if (this.holderActive) {
            TransactionSynchronizationManager.unbindResource(this.dataSource);
            if (this.connectionHolder.hasConnection() && !this.connectionHolder.isOpen()) {
                // 关闭ConnectionHolder的连接
                releaseConnection(this.connectionHolder.getConnection(), this.dataSource);
                this.connectionHolder.setConnection(null);
            }
        }
    }

    @Override
    public void resume() {
        if (this.holderActive) {
            TransactionSynchronizationManager.bindResource(this.dataSource, this.connectionHolder);
        }
    }

    @Override
    public void beforeCompletion() {
        if (!this.connectionHolder.isOpen()) {
            // 移除DataSource的连接
            TransactionSynchronizationManager.unbindResource(this.dataSource);
            this.holderActive = false;
            if (this.connectionHolder.hasConnection()) {
                releaseConnection(this.connectionHolder.getConnection(), this.dataSource);
            }
        }
    }

    @Override
    public void afterCompletion(int status) {
        if (this.holderActive) {
            TransactionSynchronizationManager.unbindResourceIfPossible(this.dataSource);
            this.holderActive = false;
            if (this.connectionHolder.hasConnection()) {
                releaseConnection(this.connectionHolder.getConnection(), this.dataSource);
                this.connectionHolder.setConnection(null);
            }
        }
        this.connectionHolder.reset();
    }
}
