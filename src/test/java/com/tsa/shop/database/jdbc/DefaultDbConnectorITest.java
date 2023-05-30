package com.tsa.shop.database.jdbc;

import com.tsa.shop.argsparser.interfaces.PropertyReader;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class DefaultDbConnectorITest {
    private final static String DB_URL = "jdbc:postgresql://192.168.0.169:5432/shop";
    private final static String DB_USER_NAME = "postgres";
    private final static String DB_PASSWORD = "password";

    @Test
    void getConnection() throws SQLException {
        PropertyReader readerDoc = Mockito.mock(PropertyReader.class);
        Connection connectionDouble = Mockito.mock(Connection.class);
        when(readerDoc.getDbUrl()).thenReturn(DB_URL);
        when(readerDoc.getDbUserName()).thenReturn(DB_USER_NAME);
        when(readerDoc.getDbPassword()).thenReturn(DB_PASSWORD);
        when(connectionDouble.isClosed()).thenReturn(false);

        var connectorSut = Mockito.mock(DefaultDbConnector.class);

        when(connectorSut.getConnection()).thenReturn(connectionDouble);

        Connection connection = connectorSut.getConnection();
        assertFalse(connection.isClosed());
    }
}