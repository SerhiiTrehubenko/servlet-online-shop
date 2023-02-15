package com.tsa.shop.domain.interfaces;

import com.tsa.shop.ServletStarter;
import com.tsa.shop.database.util.DbConnector;
import com.tsa.shop.orm.interfaces.QueryGenerator;
import com.tsa.shop.orm.services.DefaultQueryGenerator;
import com.tsa.shop.servlets.enums.HttpStatus;
import com.tsa.shop.servlets.exceptions.WebServerException;

import java.io.Serializable;
import java.lang.reflect.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public interface TsaRepository<T> {

    String PREFIX_OF_METHOD_GET = "get";
    String PARAMETER_OF_METHOD_STRING = "String";
    Properties PROPERTIES = ServletStarter.TUNER.getProperties();
    char POINTER = '.';
    QueryGenerator QUERY_GENERATOR = new DefaultQueryGenerator();

    default List<T> findAll() {
        Class<?> entityClass = getClassOfEntity();
        String query = QUERY_GENERATOR.findAll(entityClass);

        List<T> listOfEntities = new ArrayList<>();
        Map<String, Method> methodsFormResultSet;
        Map<String, Field> fieldMap = QUERY_GENERATOR.getMapOfEntityFields(entityClass);

        try (var connection = DbConnector.getConnection(PROPERTIES)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            methodsFormResultSet = getMethodsFromResultSet(resultSet);

            while (resultSet.next()) {
                T entity = getEntityInstance(entityClass, fieldMap,methodsFormResultSet, resultSet);
                listOfEntities.add(entity);
            }
        } catch (Exception e) {
            throw new WebServerException(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return listOfEntities;
    }

    default T findById(Serializable id) {
        Class<?> entityClass = getClassOfEntity();
        String query = QUERY_GENERATOR.findById(entityClass, id);
        Map<String, Method> methodsFormResultSet;
        Map<String, Field> fieldMap = QUERY_GENERATOR.getMapOfEntityFields(entityClass);

        T entity = null;

        try (var connection = DbConnector.getConnection(PROPERTIES)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            methodsFormResultSet = getMethodsFromResultSet(resultSet);

            while (resultSet.next()) {
                entity = getEntityInstance(entityClass, fieldMap,methodsFormResultSet, resultSet);
            }
        } catch (Exception e) {
            throw new WebServerException(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return entity;
    }

    default void update(T entity) {
        String updateQuery = QUERY_GENERATOR.update(entity);
        try (var connection = DbConnector.getConnection(PROPERTIES)) {
            connection.createStatement().execute(updateQuery);
        } catch (Exception e) {
            throw new WebServerException(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    default void delete(Serializable id) {
        Class<?> entityClass = getClassOfEntity();
        String deleteQuery = QUERY_GENERATOR.deleteById(entityClass, id);
        try (var connection = DbConnector.getConnection(PROPERTIES)) {
            connection.createStatement().execute(deleteQuery);
        } catch (Exception e) {
            throw new WebServerException(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    default void add(T entity) {
        String insertQuery = QUERY_GENERATOR.insert(entity);
        try (var connection = DbConnector.getConnection(PROPERTIES)) {
            connection.createStatement().execute(insertQuery);
        } catch (Exception e) {
            throw new WebServerException(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    default Class<?> getClassOfEntity() {
        Class<?> currentClass = this.getClass();
        Type interfaceOfCurrentClass = currentClass.getGenericInterfaces()[0];
        String stringNameOfGenericInterface = interfaceOfCurrentClass.getTypeName();

        int startIndex = stringNameOfGenericInterface.indexOf('<') + 1;
        int lastIndex = stringNameOfGenericInterface.length() - 1;

        String nameOfGenericType = stringNameOfGenericInterface.substring(startIndex, lastIndex);

        try {
            return Class.forName(nameOfGenericType);
        } catch (ClassNotFoundException e) {
            throw new WebServerException(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @param mapMeta a Map of key = column name, value = Variable Type of column name;
     * @return a MAP that contains key = column name, value = method of ResultSet for retrieving data from ResultSet Instance;
     */
    default Map<String, Method> getMethodsForRetrievingDataFromResult(Map<String, String> mapMeta) {
        Map<String, Method> mapOfNeededMethods = new HashMap<>();
        Method[] methods = ResultSet.class.getMethods();
        for (String keyColumnName : mapMeta.keySet()) {
            String typeOfValue = mapMeta.get(keyColumnName);
            String suffixOfNeededMethod = typeOfValue.substring(typeOfValue.lastIndexOf(POINTER) + 1);
            for (Method method : methods) {
                String methodName = method.getName();
                Parameter[] methodParameters = method.getParameters();
                String neededMethodName = PREFIX_OF_METHOD_GET + suffixOfNeededMethod;
                if (methodName.contains(neededMethodName) &&
                        methodParameters.length == 1 &&
                        methodParameters[0].toString().contains(PARAMETER_OF_METHOD_STRING)) {
                    mapOfNeededMethods.put(keyColumnName, method);
                }
            }
        }
        return mapOfNeededMethods;
    }

    default void enrichMapOfColumnNamesAndColumnTypes(ResultSet queryResult, Map<String, String> mapMeta) throws SQLException {
        ResultSetMetaData metaDataOfResultSet = queryResult.getMetaData();
        int quantityColumns = metaDataOfResultSet.getColumnCount();
        for (int i = 1; i <= quantityColumns; i++) {
            mapMeta.put(metaDataOfResultSet.getColumnName(i), metaDataOfResultSet.getColumnClassName(i));
        }
    }

    default T getEntityInstance(Class<?> entityClass,
                                Map<String, Field> fieldMap,
                                Map<String, Method> methodsFormResultSet,
                                ResultSet resultSet) throws Exception {
        @SuppressWarnings("unchecked")
        T entityInstance = (T) entityClass.getConstructor().newInstance();
        for (String keyFieldMap : fieldMap.keySet()) {
            Field entityField = fieldMap.get(keyFieldMap);
            Object entityFieldValue = methodsFormResultSet.get(keyFieldMap).invoke(resultSet, keyFieldMap);
            entityField.setAccessible(true);
            entityField.set(entityInstance, entityFieldValue);
        }
        return entityInstance;
    }

    default Map<String, Method> getMethodsFromResultSet(ResultSet resultSet) throws SQLException {
        Map<String, String> mapMeta = new HashMap<>();
        enrichMapOfColumnNamesAndColumnTypes(resultSet, mapMeta);
        return getMethodsForRetrievingDataFromResult(mapMeta);
    }
}
