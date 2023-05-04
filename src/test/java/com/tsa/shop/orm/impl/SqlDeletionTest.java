package com.tsa.shop.orm.impl;

import com.tsa.shop.domain.entity.Product;
import com.tsa.shop.orm.interfaces.NameResolver;
import com.tsa.shop.orm.interfaces.EntityClassMeta;
import com.tsa.shop.orm.interfaces.Sql;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SqlDeletionTest {

    @Test
    void shouldReturnQueryDeleteById() {
        String expectedQuery = "DELETE FROM products WHERE product_id=?;";

        EntityClassMeta meta = new DefaultEntityClassMeta(Product.class);
        NameResolver resolver = new DefaultNameResolver(meta);
        Sql generator = new SqlDeletion(resolver);

        String resultQuery = generator.generateById();

        assertEquals(expectedQuery, resultQuery);
    }
}