package com.tsa.shop.services.interfaces;

import com.tsa.shop.domain.dto.ProductDto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface ProductService {
    List<ProductDto> findAll();

    ProductDto findById(Serializable id);

    void update(Map<String, String[]> parameters);

    void delete(Serializable id);

    void add(Map<String, String[]> parameters);
}
