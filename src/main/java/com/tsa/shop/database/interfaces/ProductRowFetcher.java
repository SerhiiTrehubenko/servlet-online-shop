package com.tsa.shop.database.interfaces;

import com.tsa.shop.domain.Product;

import java.sql.ResultSet;

public interface ProductRowFetcher {
    Product getProduct(ResultSet row) throws Exception;
}
