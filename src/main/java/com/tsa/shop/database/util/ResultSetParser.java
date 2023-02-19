package com.tsa.shop.database.util;

import com.tsa.shop.servlets.enums.HttpStatus;
import com.tsa.shop.servlets.exceptions.WebServerException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ResultSetParser<T> {
    private final static String PREFIX_OF_GET_METHOD = "get";
    private final static char DOT_SPLITERATOR = '.';
    private final Class<?> entityClass;

    public ResultSetParser(Class<?> repositoryClass) {
        this.entityClass = getEntityClassFromGenericSuperclass(repositoryClass);
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public T getEntityFromRow(Map<String, Field> entityColumns,
                              Map<String, Method> methodsFromResultSet,
                              ResultSet resultSet) throws Exception {
        @SuppressWarnings("unchecked")
        T entityInstance = (T) entityClass.getConstructor().newInstance();
        for (Map.Entry<String, Field> entry : entityColumns.entrySet()) {
            String columnName = entry.getKey();
            Field entityColumn = entry.getValue();
            Object columnValueFromResultSet = methodsFromResultSet.get(columnName).invoke(resultSet, columnName);
            entityColumn.setAccessible(true);
            entityColumn.set(entityInstance, columnValueFromResultSet);
        }
        return entityInstance;
    }

    public Map<String, Method> getResultSetMethods(ResultSet resultSet) throws SQLException {
        Map<String, String> retrievedColumns = getRetrievedColumnNamesAndColumnTypes(resultSet);

        return getResultSetMethodsForRetrievingData(retrievedColumns);
    }

    Class<?> getEntityClassFromGenericSuperclass(Class<?> clazz) {
        Type genericSuperclass = clazz.getGenericSuperclass();
        String stringNameOfGenericSuperclass = genericSuperclass.getTypeName();

        int startIndexOfEntityClassName = stringNameOfGenericSuperclass.indexOf('<') + 1;
        int lastIndexOfEntityClassName = stringNameOfGenericSuperclass.length() - 1;

        String nameOfEntityClass = stringNameOfGenericSuperclass.substring(startIndexOfEntityClassName, lastIndexOfEntityClassName);

        try {
            return Class.forName(nameOfEntityClass);
        } catch (ClassNotFoundException e) {
            throw new WebServerException(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    Map<String, String> getRetrievedColumnNamesAndColumnTypes(ResultSet queryResult) throws SQLException {
        Map<String, String> mapMeta = new HashMap<>();
        ResultSetMetaData metaDataOfResultSet = queryResult.getMetaData();
        int quantityColumns = metaDataOfResultSet.getColumnCount();
        for (int i = 1; i <= quantityColumns; i++) {
            mapMeta.put(metaDataOfResultSet.getColumnName(i), metaDataOfResultSet.getColumnClassName(i));
        }
        return mapMeta;
    }

    Map<String, Method> getResultSetMethodsForRetrievingData(Map<String, String> retrievedColumnsFromResultSet) {

        Method[] publicMethodsOfResultSet = ResultSet.class.getMethods();
        Map<String, Method> filteredMethods = getMethodsWithStringParameter(publicMethodsOfResultSet);

        return retrievedColumnsFromResultSet
                .entrySet()
                .stream()
                .peek(
                        entry -> {
                            String valueTypeOfColumn = entry.getValue();
                            String suffixOfGetMethod = valueTypeOfColumn.substring(valueTypeOfColumn.lastIndexOf(DOT_SPLITERATOR) + 1);
                            String requiredMethodName = PREFIX_OF_GET_METHOD + suffixOfGetMethod;
                            entry.setValue(requiredMethodName);
                        })
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> filteredMethods.get(entry.getValue())));
    }

    Map<String, Method> getMethodsWithStringParameter(Method[] publicMethodsOfResultSet) {
        return Arrays.stream(publicMethodsOfResultSet)
                .filter(
                        method -> {
                            Parameter[] methodParameters = method.getParameters();
                            return methodParameters.length == 1 &&
                                    String.class.isAssignableFrom(methodParameters[0].getType());
                        })
                .collect(Collectors.toMap(Method::getName, method -> method));
    }
}
