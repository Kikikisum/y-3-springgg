package com.example.y3spring.transaction.dataSource;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.lang.Nullable;

import java.sql.Connection;

public abstract class SingleConnectionDataSource implements SmartDataSource, DisposableBean {

    private boolean suppressClose;

    @Nullable
    private Boolean autoCommit;

    @Nullable
    private Connection target;

    @Nullable
    private Connection connection;

    private final Object connectionMonitor = new Object();


    public SingleConnectionDataSource() {
    }

    @Override
    public boolean shouldClose(Connection con)
    {
        synchronized (this.connectionMonitor) {
            return (con != this.connection && con != this.target);
        }
    }

}
