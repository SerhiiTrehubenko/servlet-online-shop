package com.tsa.shop.database.dao;

import com.tsa.shop.domain.entity.Product;

import java.sql.Date;
import java.sql.ResultSet;

public class DefaultProductRowFetcher implements ProductRowFetcher {
    @Override
    public Product getProduct(ResultSet row) throws Exception {
        Long id = row.getLong(1);
        String name = row.getString(2);
        double price = row.getDouble(3);
        Date date = row.getDate(4);
        return new Product(id, name, price, date);
    }
}
