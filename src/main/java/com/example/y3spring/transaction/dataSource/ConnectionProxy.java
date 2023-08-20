package com.example.y3spring.transaction.dataSource;

import java.sql.Connection;

public interface ConnectionProxy extends Connection {

    Connection getTargetConnection();
}
