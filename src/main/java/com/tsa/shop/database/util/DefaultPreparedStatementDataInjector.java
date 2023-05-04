package com.tsa.shop.database.util;

import com.tsa.shop.database.interfaces.PreparedStatementDataInjector;
import com.tsa.shop.orm.interfaces.EntityClassMeta;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

public class DefaultPreparedStatementDataInjector<T> implements PreparedStatementDataInjector<T> {

    private final static int FIRST_INDEX = 1;
    private final Field idField;
    private final List<Field> fields;
    private Iterator<Field> iterator;

    public DefaultPreparedStatementDataInjector(EntityClassMeta classMeta) {
        this.idField = classMeta.getIdField();
        this.fields = classMeta.getFields();
        this.iterator = fields.iterator();
    }

    @Override
    public PreparedStatement injectColumns(PreparedStatement statement, T sourceData) {
        try {
            for (int parameterIndex = 0; parameterIndex < fields.size(); parameterIndex++) {
                Object value = getValue(sourceData);
                Method psMethod = getPsMethod(value);
                psMethod.invoke(statement, adjust(parameterIndex), value);
            }
        } catch (Exception e) {
            refreshIterator();
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        refreshIterator();
        return statement;
    }

    private void refreshIterator() {
        iterator = fields.iterator();
    }

    private Object getValue(T sourceData) throws Exception {
        Field entityField = iterator.next();
        entityField.setAccessible(true);
        return entityField.get(sourceData);
    }

    private Method getPsMethod(Object value) {
        Class<?> type = value.getClass();
        return JDBCMethodConnector.getPreparedStatementMethod(type);
    }

    private int adjust(int parameterIndex) {
        return parameterIndex + FIRST_INDEX;
    }

    public PreparedStatement injectColumnsAndId(PreparedStatement statement, T entity) {
        try {
            PreparedStatement payloadStatement = injectColumns(statement, entity);
            return injectId(payloadStatement, (Serializable) idField.get(entity));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PreparedStatement injectId(PreparedStatement statement, Serializable resolvedId) {
        try {
            int indexIdParameter = getPlaceHoldersCount(statement);
            Method psMethod = getPsMethod(resolvedId);
            psMethod.invoke(statement, indexIdParameter, resolvedId);
            return statement;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private int getPlaceHoldersCount(PreparedStatement statement) throws SQLException {
        return statement.getParameterMetaData().getParameterCount();
    }
}
