package com.tsa.shop.database.implementation;

import com.tsa.shop.database.interfaces.ProductRowFetcher;
import com.tsa.shop.domain.Product;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class DefaultProductRowFetcher implements ProductRowFetcher {
    @Override
    public Product getProduct(ResultSet row) throws Exception {
        Long id = row.getLong(1);
        String name = row.getString(2);
        double price = row.getDouble(3);
        Timestamp date = row.getTimestamp(4);
        return new Product(id, name, price, date);
    }
}
