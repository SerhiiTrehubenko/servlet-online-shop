package com.tsa.shop.service;

import com.tsa.shop.domain.ProductDto;
import com.tsa.shop.domain.DtoExtractor;
import com.tsa.shop.domain.HttpStatus;
import com.tsa.shop.domain.WebServerException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

public class ProductDtoExtractor implements DtoExtractor {
    private final static int VALUE = 0;

    @Override
    public ProductDto getFullDtoInstanceFrom(Map<String, String[]> parameters) {
        Long id = Long.parseLong(parameters.get("id")[VALUE]);
        String name = getName(parameters);
        double price = parseDouble(parameters);
        Timestamp date = Timestamp.valueOf(parameters.get("date")[VALUE]);
        String description = getDescription(parameters);
        return new ProductDto(id, name, price, date, description);
    }

    private String getName(Map<String, String[]> parameters) {
        return parameters.get("name")[VALUE];
    }
    private String getDescription(Map<String, String[]> parameters) {
        return Objects.toString(parameters.get("description")[VALUE]);
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
        String description = getDescription(parameters);
        ProductDto productDto = new ProductDto();
        productDto.setName(name);
        productDto.setPrice(price);
        productDto.setDate(date);
        productDto.setDescription(description);
        return productDto;
    }

    @Override
    public DtoExtractor createInstance() {
        return new ProductDtoExtractor();
    }
}
