package com.tsa.shop.database.repo;

import com.tsa.shop.database.util.DbConnector;
import com.tsa.shop.database.util.ResultSetParser;
import com.tsa.shop.orm.interfaces.QueryGenerator;
import com.tsa.shop.orm.services.DefaultQueryGenerator;
import com.tsa.shop.servlets.enums.HttpStatus;
import com.tsa.shop.servlets.exceptions.WebServerException;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public abstract class AbstractTsaRepository<T> {
    private final QueryGenerator queryGenerator = new DefaultQueryGenerator();
    private final ResultSetParser<T> resultSetParser = new ResultSetParser<>(getClass());
    private final DbConnector connector = new DbConnector();

    public List<T> findAll() {
        Class<?> entityClass = resultSetParser.getEntityClass();
        String queryFindAll = queryGenerator.findAll(entityClass);

        List<T> entities = getDataFromDb(queryFindAll);

        if (entities.isEmpty()) {
            throw new WebServerException("Database is empty", HttpStatus.NOT_FOUND);
        }
        return entities;
    }

    public T findById(Serializable id) {
        Class<?> entityClass = resultSetParser.getEntityClass();
        String queryFindById = queryGenerator.findById(entityClass, id);

        List<T> entity = getDataFromDb(queryFindById);

        if (entity.isEmpty()) {
            throw new WebServerException("There is no " + entityClass.getSimpleName() + ", with id: " + id, HttpStatus.NOT_FOUND);
        }
        return entity.get(0);
    }

    public void update(T entity) {
        String updateQuery = queryGenerator.update(entity);

        try (var connection = connector.getDbConnection()) {
            connection.createStatement().execute(updateQuery);
        } catch (SQLException e) {
            throw new WebServerException(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void delete(Serializable id) {
        Class<?> entityClass = resultSetParser.getEntityClass();
        String deleteQuery = queryGenerator.deleteById(entityClass, id);

        try (var connection = connector.getDbConnection()) {
            connection.createStatement().execute(deleteQuery);
        } catch (Exception e) {
            throw new WebServerException(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void add(T entity) {
        String insertQuery = queryGenerator.insert(entity);

        try (var connection = connector.getDbConnection()) {
            connection.createStatement().execute(insertQuery);
        } catch (Exception e) {
            throw new WebServerException(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<T> getDataFromDb(String query) {
        List<T> entitiesFromDb = new ArrayList<>();
        Map<String, Field> entityColumns = queryGenerator.getEntityColumns(resultSetParser.getEntityClass());

        try (var connection = connector.getDbConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            Map<String, Method> resultSetMethods = resultSetParser.getResultSetMethods(resultSet);

            while (resultSet.next()) {
                T entity = resultSetParser.getEntityFromRow(entityColumns, resultSetMethods, resultSet);
                entitiesFromDb.add(entity);
            }
        } catch (Exception e) {
            throw new WebServerException(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return entitiesFromDb;
    }
}
