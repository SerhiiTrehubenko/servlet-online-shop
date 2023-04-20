package com.tsa.shop.domain.mappers.interfaces;

public interface Mapper<T, E> {
    E toProductDto(T entity);

    T toProduct(E dto);
}
