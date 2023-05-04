package com.tsa.shop.database.interfaces;

import java.sql.ResultSet;

public interface EntityRowFetcher<T> {
    T getEntityFrom(ResultSet resultSet);
    Class<?> getEntityClass();
}
