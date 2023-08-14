package com.example.y3spring.jdbc.support;

import com.example.y3spring.jdbc.exception.MetaDataAccessException;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public interface DatabaseMetaDataCallback {

    /**
     * 根据具体实现来进行实现此方法
     * @param dbmd
     * @return
     * @throws SQLException
     * @throws MetaDataAccessException
     */
    Object processMetaData(DatabaseMetaData dbmd) throws SQLException, MetaDataAccessException;

}
