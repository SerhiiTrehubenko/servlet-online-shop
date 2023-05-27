package com.tsa.shop.domain.services;

import com.tsa.shop.database.repo.AbstractTsaRepository;
import com.tsa.shop.domain.interfaces.EntityService;
import com.tsa.shop.domain.mappers.interfaces.Mapper;
import com.tsa.shop.orm.interfaces.RequestDtoExtractor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class GenericProductService<T, E> implements EntityService<T, E> {

    private final AbstractTsaRepository<T> repository;
    private final Mapper<T, E> mapper;
    private final RequestDtoExtractor<E> parser;


    public GenericProductService(AbstractTsaRepository<T> repository,
                                 Mapper<T, E> mapper,
                                 RequestDtoExtractor<E> parser) {
        this.repository = repository;
        this.mapper = mapper;
        this.parser = parser;
    }

    @Override
    public List<E> findAll() {
        List<T> productList = repository.findAll();
        return productList.stream()
                .map(mapper::toProductDto).toList();
    }

    @Override
    public E findById(Serializable id) {
        T product = repository.findById(id);
        return mapper.toProductDto(product);
    }

    @Override
    public void update(Map<String, String[]> parameters) {
        T product = getProductFrom(parameters);
        repository.update(product);
    }

    private T getProductFrom(Map<String, String[]> parameters) {
        E productDto = parser.createInstance().getDtoInstanceFromParameters(parameters);
        return mapper.toProduct(productDto);
    }

    @Override
    public void delete(Serializable id) {
        repository.delete(id);
    }
    @Override
    public void add(Map<String, String[]> parameters) {
        T product = getProductFrom(parameters);
        repository.add(product);
    }
}
