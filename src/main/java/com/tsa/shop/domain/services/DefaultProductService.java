package com.tsa.shop.domain.services;

import com.tsa.shop.database.repo.AbstractTsaRepository;
import com.tsa.shop.database.repo.ProductRepository;
import com.tsa.shop.domain.dto.ProductDto;
import com.tsa.shop.domain.entity.Product;
import com.tsa.shop.domain.interfaces.EntityService;
import com.tsa.shop.domain.mappers.DefaultProductMapper;
import com.tsa.shop.domain.mappers.interfaces.Mapper;
import com.tsa.shop.orm.util.EntityParser;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class DefaultProductService implements EntityService<ProductDto> {

    private final AbstractTsaRepository<Product> repository;
    private final Mapper<ProductDto, Product> mapper;
    private final EntityParser parser;

    public DefaultProductService() {
        this(new ProductRepository(), new DefaultProductMapper(), new EntityParser());
    }

    public DefaultProductService(AbstractTsaRepository<Product> repository,
                                 Mapper<ProductDto, Product> mapper, EntityParser parser) {
        this.repository = repository;
        this.mapper = mapper;
        this.parser = parser;
    }

    @Override
    public List<ProductDto> findAll() {
        List<Product> productList = repository.findAll();
        return productList.stream()
                .map(mapper::toProductDto).toList();
    }

    @Override
    public ProductDto findById(Serializable id) {
        Product product = repository.findById(id);
        return mapper.toProductDto(product);
    }

    @Override
    public void update(Map<String, String[]> parameters) {
        ProductDto productDto = parser.getDtoInstance(ProductDto.class, parameters);
        Product product = mapper.toProduct(productDto);
        repository.update(product);
    }

    @Override
    public void delete(Serializable id) {
        repository.delete(id);
    }

    @Override
    public void add(Map<String, String[]> parameters) {
        ProductDto productDto = parser.getDtoInstance(ProductDto.class, parameters);
        Product product = mapper.toProduct(productDto);
        repository.add(product);
    }
}
