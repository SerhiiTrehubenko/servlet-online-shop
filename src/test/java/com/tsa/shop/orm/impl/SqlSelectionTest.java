package com.tsa.shop.orm.impl;

import com.tsa.shop.domain.entity.Product;
import com.tsa.shop.orm.interfaces.NameResolver;
import com.tsa.shop.orm.interfaces.EntityClassMeta;
import com.tsa.shop.orm.interfaces.Sql;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SqlSelectionTest {
    private final EntityClassMeta meta = new DefaultEntityClassMeta(Product.class);
    private final NameResolver resolver = new DefaultNameResolver(meta);
    private final Sql generator = new SqlSelection(resolver);

    @Test
    void shouldReturnQueryFindAll() {
        String expectedQuery = "SELECT product_name, product_price, creationdate, product_id FROM products;";

        String resultQuery = generator.generate();

        assertEquals(expectedQuery, resultQuery);
    }

    @Test
    void shouldReturnQueryFindByIdWithPlaceHolder() {
        String expectedQuery = "SELECT product_name, product_price, creationdate, product_id FROM products WHERE product_id=?;";

        String resultQuery = generator.generateById();

        assertEquals(expectedQuery, resultQuery);
    }
}