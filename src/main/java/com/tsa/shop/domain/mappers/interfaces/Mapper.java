package com.tsa.shop.domain.mappers.interfaces;

public interface Mapper<T, E> {
    T doMapping (E entity);

    E doRemapping (T dto);
}
