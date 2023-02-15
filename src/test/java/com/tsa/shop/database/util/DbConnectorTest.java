package com.tsa.shop.database.util;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DbConnectorTest {

    @Test
    void getConnection() {
        try (Connection connection = DbConnector.getConnection(PropertyParser.getProperties())) {
            assertFalse(connection.isClosed());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    @Test
    void testSqlDate() {
        System.out.println(new Date(System.currentTimeMillis()));

    }

    @Test
    void testParseStringToLong() {
//        Long id = Long.getLong("12"); NULL
        Long id = Long.parseLong("12");
        System.out.println(id);
    }
}