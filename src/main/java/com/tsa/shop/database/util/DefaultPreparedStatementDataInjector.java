package com.tsa.shop.database.util;

import com.tsa.shop.database.interfaces.PreparedStatementDataInjector;
import com.tsa.shop.orm.interfaces.EntityClassMeta;
import com.tsa.shop.servlets.enums.HttpStatus;
import com.tsa.shop.servlets.exceptions.WebServerException;

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
            throw new WebServerException("There was a problem during column values injection into PreparedStatement", e, HttpStatus.INTERNAL_SERVER_ERROR, this);
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
        } catch (WebServerException e) {
            throw e;
        } catch (Exception e) {
            throw new WebServerException("There was a problem during ID retrieving from : [%s]".formatted(entity.getClass().getName()), e, HttpStatus.INTERNAL_SERVER_ERROR, this);
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
            throw new WebServerException("There was a problem during injection ID into PreparedStatement", e, HttpStatus.INTERNAL_SERVER_ERROR, this);
        }
    }

    private int getPlaceHoldersCount(PreparedStatement statement) throws SQLException {
        return statement.getParameterMetaData().getParameterCount();
    }
}