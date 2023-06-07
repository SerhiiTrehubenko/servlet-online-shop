package com.tsa.shop.database.jdbc;

import com.tsa.shop.database.interfaces.ProductRowFetcher;
import com.tsa.shop.domain.Product;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Objects;

public class DefaultProductRowFetcher implements ProductRowFetcher {
    @Override
    public Product getProduct(ResultSet row) throws Exception {
        Long id = row.getLong("product_id");
        String name = row.getString("product_name");
        double price = row.getDouble("product_price");
        Timestamp date = row.getTimestamp("creationdate");
        String description = row.getString("description");

        return new Product(id, name, price, date, Objects.isNull(description) ? "<Empty>" : description);
    }
}
