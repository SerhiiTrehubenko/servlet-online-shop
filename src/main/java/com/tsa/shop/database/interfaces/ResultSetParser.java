package com.tsa.shop.database.interfaces;

import java.sql.ResultSet;

public interface ResultSetParser<T> {
    T getEntityFrom(ResultSet resultSet) throws Exception;
    Class<?> getEntityClass();
}
