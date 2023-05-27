package com.tsa.shop.mapper.interfaces;

import com.tsa.shop.domain.dto.ProductDto;
import com.tsa.shop.domain.entity.Product;

public interface ProductMapper {
    ProductDto toProductDto(Product entity);

    Product toProduct(ProductDto dto);
}
