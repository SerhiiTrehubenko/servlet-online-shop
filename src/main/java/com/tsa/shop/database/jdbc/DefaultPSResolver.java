package com.tsa.shop.database.jdbc;

import com.tsa.shop.database.interfaces.PSResolver;
import com.tsa.shop.domain.HttpStatus;
import com.tsa.shop.domain.Product;
import com.tsa.shop.domain.WebServerException;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class DefaultPSResolver implements PSResolver {

    private final PreparedStatement preparedStatement;

    public DefaultPSResolver() {
        this(null);
    }

    private DefaultPSResolver(PreparedStatement preparedStatement) {
        this.preparedStatement = preparedStatement;
    }

    @Override
    public PSResolver prepareStatement(Connection connection, String query) {
        try {
            return new DefaultPSResolver(connection.prepareStatement(query));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResultSet executeQuery() {
        try {
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResultSet resolveFindById(Serializable incomeId) {
        try {
            preparedStatement.setLong(1, Long.parseLong(Objects.toString(incomeId)));
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void resolveUpdate(Product product) {
        try {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setString(3, product.getDescription());
            preparedStatement.setLong(4, product.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void resolveDelete(Product product) {
        try {
            preparedStatement.setLong(1, product.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void resolveInsert(Product product) {
        try {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setTimestamp(3, product.getDate());
            preparedStatement.setString(4, product.getDescription());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResultSet resolveFindByCriteria(Serializable criteria) {
        try {
            String regEx = "%" + criteria + "%";
            preparedStatement.setString(1, regEx);
            preparedStatement.setString(2, regEx);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void execute() {
        try {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new WebServerException(e.getLocalizedMessage(), e, HttpStatus.BAD_REQUEST, this);
        }
    }

    @Override
    public void close() throws Exception {
        preparedStatement.close();
    }
}
