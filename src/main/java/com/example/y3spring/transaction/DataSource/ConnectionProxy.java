package com.example.y3spring.transaction.DataSource;

import java.sql.Connection;

public interface ConnectionProxy extends Connection {

    Connection getTargetConnection();
}
