package com.tsa.shop.database.interfaces;

import com.tsa.shop.domain.Product;

import java.io.Serializable;
import java.util.List;

public interface ProductDao {
    List<Product> findAll();
    Product findById(Serializable id);
    void update(Product product);
    void delete(Product product);
    void add(Product product);

    List<Product> findByCriteria(String criteria);
}
