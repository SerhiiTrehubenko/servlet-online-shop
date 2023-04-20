package com.tsa.shop.orm.impl;

import com.tsa.shop.domain.entity.Product;
import com.tsa.shop.orm.impl.DefaultEntityClassMeta;
import com.tsa.shop.orm.impl.SqlInsertion;
import com.tsa.shop.orm.interfaces.EntityClassMeta;
import com.tsa.shop.orm.interfaces.Sql;
import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

class SqlInsertionTest {
    EntityClassMeta meta = new DefaultEntityClassMeta(Product.class);
    Sql generator = new SqlInsertion(meta);

    @Test
    void shouldThrowExceptionWhenInstanceIsNull() {
        assertThrows(NullPointerException.class, () -> generator.generateByObject(null));
    }

    @Test
    void shouldThrowExceptionWhenInstanceIsDifferentFromParsedClass() {
        assertThrows(RuntimeException.class, () -> generator.generateByObject(""));
    }

    @Test
    void shouldThrowExceptionWhenInstanceIsObjectClassInstance() {
        assertThrows(RuntimeException.class, () -> generator.generateByObject(new Object()));
    }

    @Test
    void shouldReturnValidInsertQuery() {
        String expectedQuery = "INSERT INTO products (product_name, product_price, creationdate) VALUES ('comp', 10.25, '2023-03-31');";

        Product product = createProduct();

        String resultQuery = generator.generateByObject(product);

        assertEquals(expectedQuery, resultQuery);
    }

    public static Product createProduct() {
        return new Product(10L,"comp", 10.25, Date.valueOf("2023-03-31"));
    }
}