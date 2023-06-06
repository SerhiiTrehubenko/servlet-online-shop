package com.tsa.shop.database;

import com.tsa.shop.argsparser.interfaces.PropertyReader;
import com.tsa.shop.database.interfaces.DbConnector;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;


public class BasicDataSourceAdapter extends BasicDataSource implements DbConnector {

    public BasicDataSourceAdapter(PropertyReader propertyReader) {
        super.setUrl(propertyReader.getDbUrl());
        super.setUsername(propertyReader.getDbUserName());
        super.setPassword(propertyReader.getDbPassword());
    }

    @Override
    public Connection getConnection() {
        try {
            return super.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
