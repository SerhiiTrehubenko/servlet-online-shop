package com.tsa.shop.database.util;

import com.tsa.shop.database.interfaces.DbConnector;
import com.tsa.shop.domain.argsparser.interfaces.PropertyReader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DefaultDbConnector implements DbConnector {
    private final PropertyReader propertyReader;

    public DefaultDbConnector(PropertyReader propertyReader) {
        this.propertyReader = propertyReader;
    }

    @Override
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(propertyReader.getDbUrl(),
                    propertyReader.getDbUserName(),
                    propertyReader.getDbPassword());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
