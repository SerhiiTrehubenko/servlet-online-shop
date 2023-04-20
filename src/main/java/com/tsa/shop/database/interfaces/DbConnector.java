package com.tsa.shop.database.interfaces;

import java.sql.Connection;

public interface DbConnector {
    Connection getConnection();
}
