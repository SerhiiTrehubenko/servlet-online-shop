package com.tsa.shop.domain.mappers.interfaces;

public interface Mapper<T, E> {
    T toProductDto(E entity);

    E toProduct(T dto);
}
