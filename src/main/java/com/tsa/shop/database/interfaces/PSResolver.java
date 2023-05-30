package com.tsa.shop.database.interfaces;

import com.tsa.shop.domain.Product;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;

public interface PSResolver extends AutoCloseable {
    PSResolver prepareStatement(Connection connection, String query);

    ResultSet executeQuery();

    ResultSet resolveFindById(Serializable incomeId);

    void resolveUpdate(Product product);

    void resolveDelete(Product product);
    void resolveInsert(Product product);

    void execute();
}
