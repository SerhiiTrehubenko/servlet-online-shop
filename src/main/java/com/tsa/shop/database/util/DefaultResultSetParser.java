package com.tsa.shop.database.util;

import com.tsa.shop.database.interfaces.ResultSetParser;
import com.tsa.shop.orm.interfaces.EntityClassMeta;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DefaultResultSetParser<T> implements ResultSetParser<T> {
    private final static String PREFIX_OF_GET_METHOD = "get";
    private final static char DOT_SPLITERATOR = '.';
    private final static int SKIP_DOT_SPLITERATOR = 1;
    private final static int START_ITERATION = 1;
    private final static int REQUIRED_ONE_PARAMETER = 1;
    private final static int THR_FIRST_PARAMETER = 0;


    private final EntityClassMeta meta;

    public DefaultResultSetParser(EntityClassMeta meta) {
        this.meta = meta;
    }

    public Class<?> getEntityClass() {
        return meta.getClassToParse();
    }

    public T getEntityFrom(ResultSet resultSetWithData) throws Exception {
        Map<Field, Object> rowValues = getEntityValues(resultSetWithData);

        return getEntityWithData(rowValues);
    }

    Map<Field, Object> getEntityValues(ResultSet resultSetWithData) throws Exception {
        Map<String, Object> rowValues = getValuesFromRow(resultSetWithData);
        Set<Map.Entry<String, Field>> entityColumns = meta.getAllEntityColumns();

        return entityColumns.stream()
                .collect(Collectors.toMap(Map.Entry::getValue, entry -> rowValues.get(entry.getKey())));
    }

    Map<String, Object> getValuesFromRow(ResultSet resultSetWithData) throws Exception {
        Map<String, Method> retrievingMethods = getResultSetMethodsForFetchingData(resultSetWithData);

        return retrievingMethods.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> this.getValue(entry, resultSetWithData)));
    }

    Map<String, Method> getResultSetMethodsForFetchingData(ResultSet resultSetWithData) throws Exception {
        Map<String, String> columnsMeta = getColumnNamesAndTypes(resultSetWithData);

        Map<String, Method> filteredMethods = getResultSetMethodsWithOneStringParameter();

        return columnsMeta
                .entrySet()
                .stream()
                .peek(entry -> entry.setValue(convertColumnTypeToMethodName(entry.getValue())))
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> filteredMethods.get(entry.getValue())));
    }

    Map<String, String> getColumnNamesAndTypes(ResultSet resultSetWithData) throws SQLException {
        Map<String, String> columnsMeta = new HashMap<>();
        ResultSetMetaData metaDataOfResultSet = resultSetWithData.getMetaData();
        int numberColumns = metaDataOfResultSet.getColumnCount();
        for (int columnOrderNumber = START_ITERATION; columnOrderNumber <= numberColumns; columnOrderNumber++) {
            columnsMeta.put(metaDataOfResultSet.getColumnName(columnOrderNumber), metaDataOfResultSet.getColumnClassName(columnOrderNumber));
        }
        return columnsMeta;
    }

    Map<String, Method> getResultSetMethodsWithOneStringParameter() {
        Method[] publicMethodsOfResultSet = ResultSet.class.getMethods();
        return Arrays.stream(publicMethodsOfResultSet)
                .filter(
                        method -> {
                            Parameter[] methodParameters = method.getParameters();
                            return methodParameters.length == REQUIRED_ONE_PARAMETER &&
                                    String.class.isAssignableFrom(methodParameters[THR_FIRST_PARAMETER].getType());
                        })
                .collect(Collectors.toMap(Method::getName, method -> method));
    }

    String convertColumnTypeToMethodName(String columnType) {
        String suffixOfGetMethod = columnType.substring(columnType.lastIndexOf(DOT_SPLITERATOR) + SKIP_DOT_SPLITERATOR);
        return PREFIX_OF_GET_METHOD + suffixOfGetMethod;
    }

    Object getValue(Map.Entry<String, Method> entry, ResultSet resultSetWithData) {
        try {
            return entry.getValue().invoke(resultSetWithData, entry.getKey());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private T getEntityWithData(Map<Field, Object> rowValues) throws Exception {
        T entityInstance = getEntityInstance();

        for (Map.Entry<Field, Object> columnEntry : rowValues.entrySet()) {

            Field column = columnEntry.getKey();
            Object value = columnEntry.getValue();

            column.setAccessible(true);
            column.set(entityInstance, value);
        }
        return entityInstance;
    }

    @SuppressWarnings("unchecked")
    private T getEntityInstance() throws Exception {
        return (T) getEntityClass().getConstructor().newInstance();
    }
}
