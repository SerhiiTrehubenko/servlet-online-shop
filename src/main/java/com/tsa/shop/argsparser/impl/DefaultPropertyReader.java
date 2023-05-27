package com.tsa.shop.argsparser.impl;

import com.tsa.shop.argsparser.interfaces.EnvironmentVariablesContext;
import com.tsa.shop.argsparser.enums.Property;
import com.tsa.shop.argsparser.interfaces.PropertyReader;

import java.io.Serializable;
import java.util.Objects;
import java.util.Properties;

public class DefaultPropertyReader implements PropertyReader {

    private static final String DB_URL = "dbUrl";
    private static final String DB_NAME = "dbName";
    private static final String USER_NAME = "userName";
    private static final String PASSWORD = "password";
    private final static String FORWARD_SLASH = "/";

    private final Properties properties;
    private final int port;

    public DefaultPropertyReader(EnvironmentVariablesContext variablesContext) {
        Serializable incomeProperties = variablesContext.getProperty(Property.FILE_PROPERTY);
        if (Properties.class.isAssignableFrom(incomeProperties.getClass())) {
            this.properties = (Properties) incomeProperties;
        } else {
            throw new RuntimeException("Provided object: [%s] is not the [%s] type".formatted(incomeProperties.getClass().getName(),
                    Properties.class.getName()));
        }
        this.port = (int) variablesContext.getProperty(Property.PORT);
    }
    @Override
    public String getDbUrl() {
        String dbUrl = properties.getProperty(DB_URL);
        Objects.requireNonNull(dbUrl, "A Data Base URL is absent");
        String dbName = properties.getProperty(DB_NAME);

        return Objects.isNull(dbName) ? dbUrl + FORWARD_SLASH : dbUrl + FORWARD_SLASH + dbName;
    }

    @Override
    public String getDbUserName() {
        String dbUserName = properties.getProperty(USER_NAME);
        Objects.requireNonNull(dbUserName, "A Data Base USER NAME is absent");
        return dbUserName;
    }

    @Override
    public String getDbPassword() {
        String dbPassword = properties.getProperty(PASSWORD);
        Objects.requireNonNull(dbPassword, "A Data Base PASSWORD is absent");
        return dbPassword;
    }

    @Override
    public int getPort() {
        return port;
    }
}
