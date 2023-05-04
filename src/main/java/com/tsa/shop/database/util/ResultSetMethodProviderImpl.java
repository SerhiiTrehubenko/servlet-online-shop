package com.tsa.shop.database.util;

import com.tsa.shop.database.interfaces.ResultSetMethodProvider;
import com.tsa.shop.database.interfaces.ResultSetBiFunction;

import java.io.Serializable;
import java.sql.ResultSet;
import java.util.Map;

public class ResultSetMethodProviderImpl implements ResultSetMethodProvider {

    private final static Map<Class<?>, ResultSetBiFunction<ResultSet, String, Serializable>> METHODS =
            Map.of(
                    long.class, ResultSet::getLong,
                    Long.class, ResultSet::getLong,
                    double.class, ResultSet::getDouble,
                    Double.class, ResultSet::getDouble,
                    String.class, ResultSet::getString,
                    java.sql.Date.class, ResultSet::getDate
            );

    @Override
    public ResultSetBiFunction<ResultSet, String, Serializable> getMethod(Class<?> returnedType) {
        return METHODS.get(returnedType);
    }
}
