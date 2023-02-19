package com.tsa.shop.database.util;

import com.tsa.shop.servlets.util.PropertyParser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnector {
    private static PropertyParser propertyParser;

    public DbConnector() {
        this(new PropertyParser());
    }

    public DbConnector(PropertyParser propertyParser) {
        DbConnector.propertyParser = propertyParser;
    }

    public Connection getDbConnection() {
        try {
            return DriverManager.getConnection(propertyParser.getDbUrl(),
                    propertyParser.getDbUserName(),
                    propertyParser.getDbPassword());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
