package com.tsa.shop.domain.services;

import com.tsa.shop.database.repo.ProductRepository;
import com.tsa.shop.domain.dto.ProductDto;
import com.tsa.shop.domain.entity.Product;
import com.tsa.shop.domain.interfaces.EntityService;
import com.tsa.shop.domain.interfaces.TsaRepository;
import com.tsa.shop.domain.mappers.DefaultProductMapper;
import com.tsa.shop.domain.mappers.interfaces.Mapper;

import java.io.Serializable;
import java.util.List;

public class DefaultProductService implements EntityService<ProductDto> {

    private final TsaRepository<Product> repository;
    private final Mapper<ProductDto, Product> mapper;

    public DefaultProductService() {
        this.repository = new ProductRepository();
        this.mapper = new DefaultProductMapper();
    }

    @Override
    public List<ProductDto> findAll() {
        List<Product> productList = repository.findAll();
        return productList.stream()
                .map(mapper::doMapping).toList();
    }

    @Override
    public ProductDto findById(Serializable id) {
        Product product = repository.findById(id);
        return mapper.doMapping(product);
    }

    @Override
    public void update(ProductDto productDto) {
        Product product = mapper.doRemapping(productDto);
        repository.update(product);
    }

    @Override
    public void delete(Serializable id) {
        repository.delete(id);
    }

    @Override
    public void add(ProductDto productDto) {
        Product product = mapper.doRemapping(productDto);
        repository.add(product);
    }
}
