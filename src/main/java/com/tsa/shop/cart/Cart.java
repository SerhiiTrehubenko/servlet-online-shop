package com.tsa.shop.cart;

import com.tsa.shop.domain.ProductDto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Cart {
    private final Collection<ProductDto> products = Collections.synchronizedCollection(new ArrayList<>());

    public void add(ProductDto productDto) {
        products.add(productDto);
    }

    public List<ProductDto> getAll() {
        return new ArrayList<>(products);
    }
}
