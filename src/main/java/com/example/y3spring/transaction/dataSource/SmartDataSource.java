package com.example.y3spring.transaction.dataSource;

import javax.sql.DataSource;
import java.sql.Connection;

public interface SmartDataSource extends DataSource {

    boolean shouldClose(Connection con);
}
