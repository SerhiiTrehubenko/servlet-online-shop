package com.tsa.shop.orm.interfaces;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface EntityClassMeta {
    List<Field> getFields();

    Class<?> getClassToParse();

    Set<Map.Entry<String, Field>> getAllFields();

    Field getIdField();
}