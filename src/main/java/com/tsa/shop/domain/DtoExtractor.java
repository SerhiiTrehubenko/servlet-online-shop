package com.tsa.shop.domain;

import java.util.Map;

public interface DtoExtractor {
    ProductDto getFullDtoInstanceFrom(Map<String, String[]> parameters);
    ProductDto getPartialDtoInstanceFrom(Map<String, String[]> parameters);
    DtoExtractor createInstance();
}
