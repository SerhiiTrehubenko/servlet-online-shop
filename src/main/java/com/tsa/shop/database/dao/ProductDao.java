package com.tsa.shop.database.dao;

import com.tsa.shop.domain.entity.Product;

import java.io.Serializable;
import java.util.List;

public interface ProductDao {
    List<Product> findAll();
    Product findById(Serializable id);
    void update(Product product);
    void delete(Serializable id);
    void add(Product product);
}
