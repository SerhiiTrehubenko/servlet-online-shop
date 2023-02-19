package com.tsa.shop.database.util;

import com.tsa.shop.servlets.util.PropertyParser;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class DbConnectorTest {
    private static String DB_URL = "jdbc:postgresql://192.168.0.169:5432/shop";
    private static String DB_USER_NAME = "postgres";
    private static String DB_PASSWORD = "password";
    @Test
    void getConnection() {
        PropertyParser readerDoc = Mockito.mock(PropertyParser.class);
        when(readerDoc.getDbUrl()).thenReturn(DB_URL);
        when(readerDoc.getDbUserName()).thenReturn(DB_USER_NAME);
        when(readerDoc.getDbPassword()).thenReturn(DB_PASSWORD);

        var connectorSut = new DbConnector(readerDoc);

        try (Connection connection = connectorSut.getDbConnection()) {
            assertFalse(connection.isClosed());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}