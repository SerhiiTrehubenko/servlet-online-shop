package com.tsa.shop.domain.interfaces;


import java.io.Serializable;
import java.util.List;


public interface EntityService<T> {

    List<T> findAll();

    T findById(Serializable id);

    void update(T productDto);

    void delete(Serializable id);

    void add(T productDto);
}
