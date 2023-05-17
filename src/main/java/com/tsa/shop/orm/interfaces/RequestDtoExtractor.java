package com.tsa.shop.orm.interfaces;

import java.util.Map;

public interface RequestDtoExtractor<E> {
    E getDtoInstanceFromParameters(Map<String, String[]> parameters);
    RequestDtoExtractor<E> createInstance();
}
