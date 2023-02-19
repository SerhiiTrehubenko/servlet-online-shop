package com.tsa.shop.orm.services;

import com.tsa.shop.domain.entity.Product;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DefaultQueryGeneratorTest {

    DefaultQueryGenerator generator = new DefaultQueryGenerator();

    @Test
    void getMapOfEntityFields() {
       var map = generator.getEntityColumns(Product.class);

        for (Map.Entry<String, Field> stringFieldEntry : map.entrySet()) {
            System.out.println(stringFieldEntry);
        }
    }

    @Test
    void findAllProduct() {
        String query = "SELECT product_id, product_name, product_price, creationdate FROM products;";
        assertEquals(query, generator.findAll(Product.class));
    }

    @Test
    void testDeleteById() {
        String query = generator.deleteById(Product.class, 12);
        System.out.println(query);
    }

    @Test
    void testUpdateEntity() {
        var product = new Product(10L,"comp", 55.25, new Date(System.currentTimeMillis()));

        System.out.println(generator.update(product));

    }

    @Test
    void testInsert() {
        var product = new Product(10L,"comp", 55.25, new Date(System.currentTimeMillis()));
        System.out.println(generator.insert(product));
    }
}