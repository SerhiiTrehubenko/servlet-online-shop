package com.tsa.shop.orm.impl;

import com.tsa.shop.domain.entity.Product;
import com.tsa.shop.orm.interfaces.EntityClassMeta;
import com.tsa.shop.orm.interfaces.Sql;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SqlSelectionTest {
    EntityClassMeta meta = new DefaultEntityClassMeta(Product.class);
    Sql generator = new SqlSelection(meta);

    @Test
    void shouldReturnQueryFindAll() {
        String expectedQuery = "SELECT product_name, product_price, creationdate, product_id FROM products;";

        String resultQuery = generator.generate();

        assertEquals(expectedQuery, resultQuery);
    }

    @Test
    void shouldReturnQueryFindByIdAsInteger() {
        String expectedQuery = "SELECT product_name, product_price, creationdate, product_id FROM products WHERE product_id=10;";

        String resultQuery = generator.generateById(10);

        assertEquals(expectedQuery, resultQuery);
    }

    @Test
    void shouldReturnQueryFindByIdAsDouble() {
        String expectedQuery = "SELECT product_name, product_price, creationdate, product_id FROM products WHERE product_id=10.2;";

        String resultQuery = generator.generateById(10.20);

        assertEquals(expectedQuery, resultQuery);
    }

    @Test
    void shouldReturnQueryFindByIdAsString() {
        String expectedQuery = "SELECT product_name, product_price, creationdate, product_id FROM products WHERE product_id='computer 10';";

        String resultQuery = generator.generateById("computer 10");

        assertEquals(expectedQuery, resultQuery);
    }
}