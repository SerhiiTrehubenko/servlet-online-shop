package com.tsa.shop.orm.impl;

import com.tsa.shop.domain.entity.Product;
import com.tsa.shop.orm.interfaces.AbstractSqlFactory;
import com.tsa.shop.orm.interfaces.AbstractSqlGenerator;
import com.tsa.shop.orm.interfaces.NameResolver;
import com.tsa.shop.orm.interfaces.EntityClassMeta;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultAbstractSqlFactoryTest {

    private final EntityClassMeta meta = new DefaultEntityClassMeta(Product.class);
    private final NameResolver resolver = new DefaultNameResolver(meta);
    private final AbstractSqlFactory factory = new DefaultSqlFactory(resolver);
    private final AbstractSqlGenerator generator = new DefaultSqlGenerator(factory);

    @Test
    void shouldReturnValidQueryDeleteById() {
        String expectedQuery = "DELETE FROM products WHERE product_id=?;";

        String resultQuery = generator.deleteById();

        assertEquals(expectedQuery, resultQuery);
    }

    @Test
    void shouldReturnValidInsertQuery() {
        String expectedQuery = "INSERT INTO products (product_name, product_price, creationdate) VALUES (?, ?, ?);";

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
        String expectedQuery = "SELECT product_name, product_price, creationdate, product_id FROM products WHERE product_id=?;";

        String resultQuery = generator.findById();

        assertEquals(expectedQuery, resultQuery);
    }

    @Test
    void shouldReturnQueryForUpdate() {
        String expectedQuery = "UPDATE products SET product_name=?, product_price=?, creationdate=? WHERE product_id=?;";

        Product product = SqlInsertionTest.createProduct();
        String resultQuery = generator.update(product);

        assertEquals(expectedQuery, resultQuery);
    }
}