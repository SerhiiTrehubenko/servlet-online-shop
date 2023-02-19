package com.tsa.shop.database.repo;

import com.tsa.shop.domain.entity.Product;

import java.util.*;

public class ProductRepository extends AbstractTsaRepository<Product> {

    public List<Product> findAll() {
        return super.findAll();
    }

    public void update(Product entity) {
        super.update(entity);
    }

    public void delete(Long id) {
        super.delete(id);
    }

    public void add(Product entity) {
        super.add(entity);
    }
}
