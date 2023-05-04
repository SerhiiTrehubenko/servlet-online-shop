package com.tsa.shop.database.interfaces;

import java.io.Serializable;
import java.sql.ResultSet;

public interface ResultSetMethodProvider {
    ResultSetBiFunction<ResultSet, String, Serializable> getMethod(Class<?> returnedType);
}
