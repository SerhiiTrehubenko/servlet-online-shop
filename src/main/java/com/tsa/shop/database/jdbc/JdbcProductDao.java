package com.tsa.shop.database.jdbc;

import com.tsa.shop.database.interfaces.DbConnector;
import com.tsa.shop.database.interfaces.PSResolver;
import com.tsa.shop.database.interfaces.ProductDao;
import com.tsa.shop.database.interfaces.ProductRowFetcher;
import com.tsa.shop.domain.HttpStatus;
import com.tsa.shop.domain.Product;
import com.tsa.shop.domain.WebServerException;

import java.io.Serializable;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

public class JdbcProductDao implements ProductDao {

    private final DbConnector connector;
    private final PSResolver psResolver;
    private final ProductRowFetcher productRowFetcher;

    public JdbcProductDao(DbConnector connector, PSResolver psResolver, ProductRowFetcher productRowFetcher) {
        this.connector = connector;
        this.psResolver = psResolver;
        this.productRowFetcher = productRowFetcher;
    }

    @Override
    public List<Product> findAll() {
        String findAllQuery = QueryProvider.PRODUCT_FIND_All.getQuery();
        List<Product> products = new LinkedList<>();
        try (var connection = connector.getConnection();
             var statment = psResolver.prepareStatement(connection, findAllQuery);
             ResultSet rows = statment.executeQuery()) {

            while (rows.next()) {
                products.add(productRowFetcher.getProduct(rows));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        checkEmptyDb(products);
        return products;
    }

    private void checkEmptyDb(List<Product> products) {
        if (products.isEmpty()) {
            throw new WebServerException("the Db is empty", HttpStatus.NOT_FOUND, this);
        }
    }

    @Override
    public Product findById(Serializable incomeId) {
        String findByIdQuery = QueryProvider.PRODUCT_FIND_BY_ID.getQuery();
        Product product = null;
        try (var connection = connector.getConnection();
             var statment = psResolver.prepareStatement(connection, findByIdQuery);
             ResultSet rows = statment.resolveFindById(incomeId)) {

            while (rows.next()) {
                product = productRowFetcher.getProduct(rows);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        checkPresence(product, incomeId);
        return product;
    }

    private void checkPresence(Product product, Serializable incomeId) {
        if (Objects.isNull(product)) {
            throw new WebServerException("A Product with id:[%s] has not be found".formatted(Objects.toString(incomeId)), HttpStatus.NOT_FOUND, this);
        }
    }

    @Override
    public void update(Product product) {
        String updateQuery = QueryProvider.PRODUCT_UPDATE.getQuery();
        process(PSResolver::resolveUpdate, product, updateQuery);
    }

    @Override
    public void delete(Product product) {
        String updateQuery = QueryProvider.PRODUCT_DELETE.getQuery();
        process(PSResolver::resolveDelete, product, updateQuery);
    }

    @Override
    public void add(Product product) {
        String updateQuery = QueryProvider.PRODUCT_INSERT.getQuery();
        process(PSResolver::resolveInsert, product, updateQuery);
    }

    private void process(BiConsumer<PSResolver, Product> biConsumer, Product product, String updateQuery) {
        try (var connection = connector.getConnection();
             var statment = psResolver.prepareStatement(connection, updateQuery)) {

            biConsumer.accept(statment, product);
            statment.execute();
        } catch (WebServerException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
