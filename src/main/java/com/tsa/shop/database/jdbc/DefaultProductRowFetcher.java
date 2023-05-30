package com.tsa.shop.database.jdbc;

import com.tsa.shop.database.interfaces.ProductRowFetcher;
import com.tsa.shop.domain.Product;

import java.sql.ResultSet;
import java.sql.Timestamp;

public class DefaultProductRowFetcher implements ProductRowFetcher {
    @Override
    public Product getProduct(ResultSet row) throws Exception {
        Long id = row.getLong("product_id");
        String name = row.getString("product_name");
        double price = row.getDouble("product_price");
        Timestamp date = row.getTimestamp("creationdate");
        return new Product(id, name, price, date);
    }
}
