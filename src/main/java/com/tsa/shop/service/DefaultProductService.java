package com.tsa.shop.service;

import com.tsa.shop.database.interfaces.ProductDao;
import com.tsa.shop.domain.ProductDto;
import com.tsa.shop.domain.Product;
import com.tsa.shop.domain.DtoExtractor;
import com.tsa.shop.domain.ProductMapper;
import com.tsa.shop.domain.ProductService;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class DefaultProductService implements ProductService {

    private final ProductDao productDao;
    private final ProductMapper productMapper;
    private final DtoExtractor dtoExtractor;

    public DefaultProductService(ProductDao productDao, ProductMapper productMapper, DtoExtractor dtoExtractor) {
        this.productDao = productDao;
        this.productMapper = productMapper;
        this.dtoExtractor = dtoExtractor;
    }

    @Override
    public List<ProductDto> findAll() {
        List<Product> productList = productDao.findAll();
        return productList.stream()
                .map(productMapper::toProductDto).toList();
    }

    @Override
    public ProductDto findById(Serializable id) {
        Product product = productDao.findById(id);
        return productMapper.toProductDto(product);
    }

    @Override
    public void update(Map<String, String[]> parameters) {
        Product product = getProductFrom(parameters);
        productDao.update(product);
    }

    @Override
    public void delete(Serializable id) {
        Product product = productDao.findById(id);
        productDao.delete(product);
    }

    @Override
    public void add(Map<String, String[]> parameters) {
        Product product = getPartialProductFrom(parameters);
        productDao.add(product);
    }

    private Product getProductFrom(Map<String, String[]> parameters) {
        ProductDto productDto = dtoExtractor.createInstance().getFullDtoInstanceFrom(parameters);
        return productMapper.toProduct(productDto);
    }

    private Product getPartialProductFrom(Map<String, String[]> parameters) {
        ProductDto productDto = dtoExtractor.createInstance().getPartialDtoInstanceFrom(parameters);
        return productMapper.toProduct(productDto);
    }
}
