package com.tsa.shop.services.interfaces;

import com.tsa.shop.domain.dto.ProductDto;

import java.util.Map;

public interface DtoExtractor {
    ProductDto getFullDtoInstanceFrom(Map<String, String[]> parameters);
    ProductDto getPartialDtoInstanceFrom(Map<String, String[]> parameters);
    DtoExtractor createInstance();
}
