package com.tsa.shop.database.interfaces;

import com.tsa.shop.domain.entity.Product;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;

public interface PSResolver extends AutoCloseable {
    PSResolver prepareStatement(Connection connection, String query);

    ResultSet executeQuery();

    ResultSet resolveFindById(Serializable incomeId);

    void resolveUpdate(Product product);

    void resolveDelete(Serializable incomeId);
    void resolveInsert(Product product);

    void execute();
}
