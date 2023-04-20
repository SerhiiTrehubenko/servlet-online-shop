package com.tsa.shop.orm.impl;

import com.tsa.shop.domain.entity.Product;
import com.tsa.shop.orm.interfaces.AbstractSqlFactory;
import com.tsa.shop.orm.interfaces.AbstractSqlGenerator;
import com.tsa.shop.orm.interfaces.EntityClassMeta;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultAbstractSqlFactoryTest {

    EntityClassMeta meta = new DefaultEntityClassMeta(Product.class);
    AbstractSqlFactory factory = new DefaultSqlFactory(meta);
    AbstractSqlGenerator generator = new DefaultSqlGenerator(factory);

    @Test
    void shouldReturnValidQueryDeleteById() {
        String expectedQuery = "DELETE FROM products WHERE product_id=10;";

        String resultQuery = generator.deleteById(10);

        assertEquals(expectedQuery, resultQuery);
    }

    @Test
    void shouldReturnValidInsertQuery() {
        String expectedQuery = "INSERT INTO products (product_name, product_price, creationdate) VALUES ('comp', 10.25, '2023-03-31');";

        Product product = SqlInsertionTest.createProduct();

        String resultQuery = generator.add(product);

        assertEquals(expectedQuery, resultQuery);
    }

    @Test
    void shouldReturnQueryFindAll() {
        String expectedQuery = "SELECT product_name, product_price, creationdate, product_id FROM products;";

        String resultQuery = generator.findAll();

        assertEquals(expectedQuery, resultQuery);
    }

    @Test
    void shouldReturnQueryFindByIdAsInteger() {
        String expectedQuery = "SELECT product_name, product_price, creationdate, product_id FROM products WHERE product_id=10;";

        String resultQuery = generator.findById(10);

        assertEquals(expectedQuery, resultQuery);
    }

    @Test
    void shouldReturnQueryFindByIdAsDouble() {
        String expectedQuery = "SELECT product_name, product_price, creationdate, product_id FROM products WHERE product_id=10.2;";

        String resultQuery = generator.findById(10.20);

        assertEquals(expectedQuery, resultQuery);
    }

    @Test
    void shouldReturnQueryFindByIdAsString() {
        String expectedQuery = "SELECT product_name, product_price, creationdate, product_id FROM products WHERE product_id='computer 10';";

        String resultQuery = generator.findById("computer 10");

        assertEquals(expectedQuery, resultQuery);
    }

    @Test
    void shouldReturnQueryForUpdate() {
        String expectedQuery = "UPDATE products SET product_name='comp', product_price=10.25, creationdate='2023-03-31' WHERE product_id=10;";

        Product product = SqlInsertionTest.createProduct();
        String resultQuery = generator.update(product);

        assertEquals(expectedQuery, resultQuery);
    }
}