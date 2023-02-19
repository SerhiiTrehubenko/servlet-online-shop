package com.tsa.shop.domain.interfaces;


import java.io.Serializable;
import java.util.List;
import java.util.Map;


public interface EntityService<T> {

    List<T> findAll();

    T findById(Serializable id);

    void update(Map<String, String[]> parameters);

    void delete(Serializable id);

    void add(Map<String, String[]> parameters);
}
