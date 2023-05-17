package com.tsa.shop.database.repo;

import com.tsa.shop.database.interfaces.DbConnector;
import com.tsa.shop.database.interfaces.IdResolver;
import com.tsa.shop.database.interfaces.PreparedStatementDataInjector;
import com.tsa.shop.database.interfaces.EntityRowFetcher;
import com.tsa.shop.orm.interfaces.AbstractSqlGenerator;
import com.tsa.shop.servlets.enums.HttpStatus;
import com.tsa.shop.servlets.exceptions.WebServerException;

import java.io.Serializable;
import java.sql.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public abstract class AbstractTsaRepository<T> {
    private final static int THE_ONE_ENTITY = 0;
    private final AbstractSqlGenerator queryGenerator;
    private final EntityRowFetcher<T> entityRowFetcher;
    private final DbConnector connector;
    private final PreparedStatementDataInjector<T> dataInjector;
    private final IdResolver idResolver;

    protected AbstractTsaRepository(AbstractSqlGenerator queryGenerator,
                                    EntityRowFetcher<T> entityRowFetcher,
                                    DbConnector connector,
                                    PreparedStatementDataInjector<T> dataInjector,
                                    IdResolver idResolver) {
        this.queryGenerator = queryGenerator;
        this.entityRowFetcher = entityRowFetcher;
        this.connector = connector;
        this.dataInjector = dataInjector;
        this.idResolver = idResolver;
    }

    public List<T> findAll() {
        String findAll = queryGenerator.findAll();
        List<T> entities = executeFindAllQuery(findAll);
        entityNotFound(entities, "Table of [%s] is empty".formatted(getEntityName()));

        return entities;
    }

    private List<T> executeFindAllQuery(String query) {
        try (var preparedStatement = createPreparedStatement(query);
             var rows = preparedStatement.executeQuery()) {

            return getEntities(rows);

        } catch (SQLException e) {
            throw new WebServerException("There was a problem during executing [FIND ALL] query", e, HttpStatus.INTERNAL_SERVER_ERROR, this);
        }
    }

    private PreparedStatement createPreparedStatement(String query) throws SQLException {
        return connector.getConnection().prepareStatement(query);
    }

    private List<T> getEntities(ResultSet rows) {
        return Stream.iterate(rows, this::hasNext, (next) -> rows)
                .map(entityRowFetcher::getEntityFrom)
                .toList();
    }

    private boolean hasNext(ResultSet resultSet) {
        try {
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void entityNotFound(List<T> entities, String message) {
        if (entities.isEmpty()) {
            throw new WebServerException(message, HttpStatus.NOT_FOUND, this);
        }
    }

    private String getEntityName() {
        Class<?> entityClass = entityRowFetcher.getEntityClass();
        return entityClass.getSimpleName();
    }

    public T findById(Serializable id) {
        Serializable resolvedId = idResolver.resolveId(id);
        String findById = queryGenerator.findById();
        List<T> entity = executeFindByIdQuery(findById, resolvedId);

        entityNotFound(entity, "There is no [%s], with id: [%s]".formatted(getEntityName(), Objects.toString(id)));

        return entity.get(THE_ONE_ENTITY);
    }

    private List<T> executeFindByIdQuery(String query, Serializable resolvedId) {
        try (var preparedStatement = createPreparedStatement(query);
             var rows = getRowsFromDb(preparedStatement, resolvedId)) {

            return getEntities(rows);

        } catch (SQLException e) {
            throw new WebServerException("There was a problem during executing [FIND BY ID] query", e, HttpStatus.INTERNAL_SERVER_ERROR, this);
        }
    }

    private ResultSet getRowsFromDb(PreparedStatement preparedStatement, Serializable id) throws SQLException {
        PreparedStatement payloadStatement = dataInjector.injectId(preparedStatement, id);
        return payloadStatement.executeQuery();
    }

    public void update(T entity) {
        String update = queryGenerator.update(entity);
        executeUpdateQuery(update, entity);
    }

    void executeUpdateQuery(String query, T entity) {
        try (var preparedStatement = createPreparedStatement(query);
             var payloadStatement = dataInjector.injectColumnsAndId(preparedStatement, entity)) {

            payloadStatement.executeUpdate();
        } catch (SQLException e) {
            throw new WebServerException("There was a problem during executing [UPDATE] query", e, HttpStatus.INTERNAL_SERVER_ERROR, this);
        }
    }



    public void delete(Serializable id) {
        Serializable resolvedId = idResolver.resolveId(id);
        String deleteById = queryGenerator.deleteById();
        executeDeleteQuery(deleteById, resolvedId);
    }

    void executeDeleteQuery(String query, Serializable resolvedId) {
        try (var preparedStatement = createPreparedStatement(query);
             PreparedStatement payloadStatement = dataInjector.injectId(preparedStatement, resolvedId)) {

            payloadStatement.executeUpdate();
        } catch (SQLException e) {
            throw new WebServerException("There was a problem during executing [DELETE] query", e, HttpStatus.INTERNAL_SERVER_ERROR, this);
        }
    }

    public void add(T entity) {
        String add = queryGenerator.add(entity);

        executeAddQuery(add, entity);
    }

    void executeAddQuery(String query, T entity) {
        try (var preparedStatement = createPreparedStatement(query);
             PreparedStatement payloadStatement = dataInjector.injectColumns(preparedStatement, entity)) {

            payloadStatement.executeUpdate();
        } catch (SQLException e) {
            throw new WebServerException("There was a problem during executing [ADD] query", e, HttpStatus.INTERNAL_SERVER_ERROR, this);
        }
    }

}
