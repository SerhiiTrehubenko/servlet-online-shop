package com.tsa.shop.domain;

import com.tsa.shop.domain.ProductDto;
import com.tsa.shop.domain.Product;

public interface ProductMapper {
    ProductDto toProductDto(Product entity);

    Product toProduct(ProductDto dto);
}
