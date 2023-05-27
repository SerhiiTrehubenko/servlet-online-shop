package com.tsa.shop.database.implementation;

import com.tsa.shop.database.interfaces.DbConnector;
import com.tsa.shop.database.interfaces.PSResolver;
import com.tsa.shop.database.interfaces.ProductDao;
import com.tsa.shop.database.interfaces.ProductRowFetcher;
import com.tsa.shop.domain.entity.Product;

import java.io.Serializable;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class DefaultProductDao implements ProductDao {

    private final DbConnector connector;
    private final PSResolver psResolver;
    private final ProductRowFetcher productRowFetcher;

    public DefaultProductDao(DbConnector connector, PSResolver psResolver, ProductRowFetcher productRowFetcher) {
        this.connector = connector;
        this.psResolver = psResolver;
        this.productRowFetcher = productRowFetcher;
    }

    @Override
    public List<Product> findAll() {
        String findAllQuery = QueryProvider.PRODUCT_FIND_All.getQuery();
        List<Product> products = new LinkedList<>();
        try (var connection = connector.getConnection();
             var statment = psResolver.prepareStatement(connection, findAllQuery)) {
            ResultSet rows = statment.executeQuery();

            while (rows.next()) {
                products.add(productRowFetcher.getProduct(rows));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return products;
    }

    @Override
    public Product findById(Serializable incomeId) {
        String findByIdQuery = QueryProvider.PRODUCT_FIND_BY_ID.getQuery();
        Product product = null;
        try (var connection = connector.getConnection();
             var statment = psResolver.prepareStatement(connection, findByIdQuery)) {

            ResultSet rows = statment.resolveFindById(incomeId);

            while (rows.next()) {
                product = productRowFetcher.getProduct(rows);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return product;
    }

    @Override
    public void update(Product product) {
        String updateQuery = QueryProvider.PRODUCT_UPDATE.getQuery();
        try (var connection = connector.getConnection();
             var statment = psResolver.prepareStatement(connection, updateQuery)) {

            statment.resolveUpdate(product);
            statment.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Serializable incomeId) {
        String updateQuery = QueryProvider.PRODUCT_DELETE.getQuery();
        try (var connection = connector.getConnection();
             var statment = psResolver.prepareStatement(connection, updateQuery)) {


            statment.resolveDelete(incomeId);
            statment.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void add(Product product) {
        String updateQuery = QueryProvider.PRODUCT_INSERT.getQuery();
        try (var connection = connector.getConnection();
             var statment = psResolver.prepareStatement(connection, updateQuery)) {

            statment.resolveInsert(product);
            statment.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
