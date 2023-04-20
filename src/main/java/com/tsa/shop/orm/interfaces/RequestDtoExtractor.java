package com.tsa.shop.orm.interfaces;

import com.tsa.shop.servlets.exceptions.WebServerException;

import java.util.Map;

public interface RequestDtoExtractor<E> {
    E getDtoInstanceFromParameters(Map<String, String[]> parameters) throws WebServerException;
    RequestDtoExtractor<E> createInstance();
}
