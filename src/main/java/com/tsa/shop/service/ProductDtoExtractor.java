package com.tsa.shop.service;

import com.tsa.shop.domain.ProductDto;
import com.tsa.shop.domain.DtoExtractor;
import com.tsa.shop.domain.HttpStatus;
import com.tsa.shop.domain.WebServerException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;

public class ProductDtoExtractor implements DtoExtractor {
    private final static int VALUE = 0;

    @Override
    public ProductDto getFullDtoInstanceFrom(Map<String, String[]> parameters) {
        Long id = Long.parseLong(parameters.get("id")[VALUE]);
        String name = getName(parameters);
        double price = parseDouble(parameters);
        Timestamp date = Timestamp.valueOf(parameters.get("date")[VALUE]);
        return new ProductDto(id, name, price, date);
    }

    private String getName(Map<String, String[]> parameters) {
        return parameters.get("name")[VALUE];
    }

    private double parseDouble(Map<String, String[]> parameters) {
        String price = parameters.get("price")[VALUE];
        try {
            return Double.parseDouble(price);
        } catch (Exception e) {
            throw new WebServerException("Provided price: [%s] has wrong format".formatted(price), e, HttpStatus.BAD_REQUEST, this);
        }
    }

    @Override
    public ProductDto getPartialDtoInstanceFrom(Map<String, String[]> parameters) {
        String name = getName(parameters);
        double price = parseDouble(parameters);
        Timestamp date = Timestamp.valueOf(LocalDateTime.now());
        ProductDto productDto = new ProductDto();
        productDto.setName(name);
        productDto.setPrice(price);
        productDto.setDate(date);
        return productDto;
    }

    @Override
    public DtoExtractor createInstance() {
        return new ProductDtoExtractor();
    }
}
