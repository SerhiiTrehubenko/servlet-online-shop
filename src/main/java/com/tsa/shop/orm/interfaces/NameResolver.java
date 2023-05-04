package com.tsa.shop.orm.interfaces;

import java.util.List;

public interface NameResolver {
    List<String> getColumns();

    String getIdName();

    String getTableName();

    Class<?> getEntityClass();
}
