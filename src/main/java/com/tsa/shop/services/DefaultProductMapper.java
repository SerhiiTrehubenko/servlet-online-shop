package com.tsa.shop.services;

import com.tsa.shop.domain.ProductDto;
import com.tsa.shop.domain.Product;
import com.tsa.shop.domain.ProductMapper;

import java.sql.Date;
import java.util.Objects;

public class DefaultProductMapper implements ProductMapper {
    @Override
    public ProductDto toProductDto(Product entity) {
        return new ProductDto(entity.getId(), entity.getName(), entity.getPrice(), entity.getDate());
    }

    @Override
    public Product toProduct(ProductDto dto) {
        Long id = Objects.isNull(dto.getId()) ? 0L : dto.getId();
        Date date = Objects.isNull(dto.getDate()) ? new Date(System.currentTimeMillis()) : dto.getDate();

        return new Product(id, dto.getName(), dto.getPrice(), date);
    }
}
