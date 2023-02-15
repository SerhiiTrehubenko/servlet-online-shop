package com.tsa.shop.domain.mappers;

import com.tsa.shop.domain.dto.ProductDto;
import com.tsa.shop.domain.entity.Product;
import com.tsa.shop.domain.mappers.interfaces.Mapper;

import java.sql.Date;
import java.util.Objects;

public class DefaultProductMapper implements Mapper<ProductDto, Product> {

    @Override
    public ProductDto doMapping(Product entity) {
        return new ProductDto(entity.getId(), entity.getName(), entity.getPrice(), entity.getDate());
    }

    @Override
    public Product doRemapping(ProductDto dto) {
        Long id = Objects.isNull(dto.getId()) ? 0L : dto.getId();
        Date date = Objects.isNull(dto.getDate()) ? new Date(System.currentTimeMillis()) : dto.getDate();

        return new Product(id, dto.getName(), dto.getPrice(), date);
    }
}
