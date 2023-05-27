package com.tsa.shop.database.dao;

import com.tsa.shop.domain.entity.Product;

import java.sql.ResultSet;

public interface ProductRowFetcher {
    Product getProduct(ResultSet row) throws Exception;
}
