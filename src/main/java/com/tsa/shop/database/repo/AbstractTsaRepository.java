package com.tsa.shop.database.repo;

import com.tsa.shop.database.interfaces.DbConnector;
import com.tsa.shop.database.interfaces.ResultSetParser;
import com.tsa.shop.orm.interfaces.AbstractSqlGenerator;
import com.tsa.shop.servlets.enums.HttpStatus;
import com.tsa.shop.servlets.exceptions.WebServerException;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public abstract class AbstractTsaRepository<T> {
    private final static int THE_ONE_ENTITY = 0;
    private final AbstractSqlGenerator queryGenerator;
    private final ResultSetParser<T> resultSetParser;
    private final DbConnector connector;

    protected AbstractTsaRepository(AbstractSqlGenerator queryGenerator,
                                    ResultSetParser<T> resultSetParser,
                                    DbConnector connector) {
        this.queryGenerator = queryGenerator;
        this.resultSetParser = resultSetParser;
        this.connector = connector;
    }

    public List<T> findAll() {
        String findAll = queryGenerator.findAll();
        List<T> entities = executePayloadQuery(findAll);
        entityNotFound(entities, "Table of [%s] is empty".formatted(getEntityName()));

        return entities;
    }

    private List<T> executePayloadQuery(String query) {
        try (var connection = connector.getConnection()) {
            ResultSet rows = getRowsFromDb(connection, query);

            return Stream.iterate(rows, this::hasNext, (next) -> rows)
                    .map(this::getEntity)
                    .toList();

        } catch (Exception e) {
            throw new WebServerException(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    private ResultSet getRowsFromDb(Connection connection, String query) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }
    private boolean hasNext(ResultSet resultSet) {
        try {
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private T getEntity(ResultSet row) {
        try {
            return resultSetParser.getEntityFrom(row);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void entityNotFound(List<T> entities, String message) {
        if (entities.isEmpty()) {
            throw new WebServerException(message, HttpStatus.NOT_FOUND);
        }
    }

    private String getEntityName() {
        Class<?> entityClass = resultSetParser.getEntityClass();
        return entityClass.getSimpleName();
    }

    public T findById(Serializable id) {
        String findById = queryGenerator.findById(id);
        List<T> entity = executePayloadQuery(findById);

        entityNotFound(entity, "There is no [%s], with id: [%s]".formatted(getEntityName(), Objects.toString(id)));

        return entity.get(THE_ONE_ENTITY);
    }

    public void update(T entity) {
        String update = queryGenerator.update(entity);
        executeQuery(update);
    }

    private void executeQuery(String query) {
        try (var connection = connector.getConnection()) {
            var statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            throw new WebServerException(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void delete(Serializable id) {
        String deleteById = queryGenerator.deleteById(id);
        executeQuery(deleteById);
    }

    public void add(T entity) {
        String add = queryGenerator.add(entity);
        executeQuery(add);
    }
}
