package com.tsa.shop.orm.interfaces;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Map;

public interface QueryGenerator {
    String findAll (Class<?> type);
    String findById (Class<?> type, Serializable id);
    String deleteById (Class<?> type, Serializable id);

    String insert(Object object);

    String update(Object object);

    Map<String, Field> getEntityColumns(Class<?> type);
}
