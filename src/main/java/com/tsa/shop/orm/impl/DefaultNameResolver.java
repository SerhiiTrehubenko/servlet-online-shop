package com.tsa.shop.orm.impl;

import com.tsa.shop.orm.annotation.OrmAnnotations;
import com.tsa.shop.orm.interfaces.NameResolver;
import com.tsa.shop.orm.interfaces.EntityClassMeta;

import java.lang.reflect.Field;
import java.util.*;

public class DefaultNameResolver implements NameResolver {
    private final static int THE_FIRS_ELEMENT = 0;
    private final EntityClassMeta meta;

    private final Map<OrmAnnotations, List<String>> columns;

    public DefaultNameResolver(EntityClassMeta meta) {
        this.meta = meta;
        int mapCapacity = OrmAnnotations.values().length;
        columns = new HashMap<>(mapCapacity);
        resolveTableName();
        resolveIdName();
        resolveColumnsNames();
    }

    private void resolveTableName() {
        Class<?> entityClass = meta.getClassToParse();
        resolveName(entityClass);
    }

    private void resolveName(Class<?> entityClass) {
        List<String> tableName = Collections.singletonList(OrmAnnotations.TABLE.getName(entityClass));
        columns.put(OrmAnnotations.TABLE, tableName);
    }

    private void resolveIdName() {
        Field idField = meta.getIdField();
        resolveName(idField);
    }

    private void resolveName(Field idField) {
        List<String> idName = Collections.singletonList(OrmAnnotations.ID.getName(idField));
        columns.putIfAbsent(OrmAnnotations.ID, idName);
    }

    private void resolveColumnsNames() {
        List<Field> fields = meta.getFields();
        resolveName(fields);
    }

    private void resolveName(List<Field> fields) {
        List<String> names = fields.stream()
                .map(OrmAnnotations.COLUMN::getName)
                .toList();
        columns.computeIfAbsent(OrmAnnotations.COLUMN, value -> names);
    }

    @Override
    public List<String> getColumns() {
        return columns.get(OrmAnnotations.COLUMN);
    }

    @Override
    public String getIdName() {
        return columns.get(OrmAnnotations.ID).get(THE_FIRS_ELEMENT);
    }

    @Override
    public String getTableName() {
        return columns.get(OrmAnnotations.TABLE).get(THE_FIRS_ELEMENT);
    }

    @Override
    public Class<?> getEntityClass() {
        return meta.getClassToParse();
    }
}
