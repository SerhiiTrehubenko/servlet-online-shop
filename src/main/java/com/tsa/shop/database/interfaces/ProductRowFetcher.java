package com.tsa.shop.database.interfaces;

import com.tsa.shop.domain.entity.Product;

import java.sql.ResultSet;

public interface ProductRowFetcher {
    Product getProduct(ResultSet row) throws Exception;
}
