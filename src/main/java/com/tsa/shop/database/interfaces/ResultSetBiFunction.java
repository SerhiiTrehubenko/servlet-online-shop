package com.tsa.shop.database.interfaces;

@FunctionalInterface
public interface ResultSetBiFunction<T, E, U> {
    U apply(T resultSet, E columnName) throws Exception;
}
