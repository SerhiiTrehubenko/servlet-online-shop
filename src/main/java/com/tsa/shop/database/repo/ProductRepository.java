package com.tsa.shop.database.repo;

import com.tsa.shop.domain.entity.Product;
import com.tsa.shop.domain.interfaces.TsaRepository;

import java.util.*;

public class ProductRepository implements TsaRepository<Product> {

    public List<Product> findAll() {
        return TsaRepository.super.findAll();
    }

    public void update(Product entity) {
        TsaRepository.super.update(entity);
    }

    public void delete(Long id) {
        TsaRepository.super.delete(id);
    }

    public void add(Product entity) {
        TsaRepository.super.add(entity);
    }
}
