package com.tsa.shop.orm.interfaces;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface EntityClassMeta {
    String getTableName();

    List<String> getColumnsNames();

    List<String> getColumnsValues(Object entityToInsert);

    String getIdColumnName();

    Class<?> getClassToParse();

    String getId(Object objectToInsert);

    Set<Map.Entry<String, Field>> getAllEntityColumns();
}