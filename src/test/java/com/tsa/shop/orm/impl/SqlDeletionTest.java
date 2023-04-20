package com.tsa.shop.orm.impl;

import com.tsa.shop.domain.entity.Product;
import com.tsa.shop.orm.impl.DefaultEntityClassMeta;
import com.tsa.shop.orm.impl.SqlDeletion;
import com.tsa.shop.orm.interfaces.EntityClassMeta;
import com.tsa.shop.orm.interfaces.Sql;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SqlDeletionTest {

    @Test
    void shouldReturnQueryDeleteById() {
        String expectedQuery = "DELETE FROM products WHERE product_id=10;";

        EntityClassMeta meta = new DefaultEntityClassMeta(Product.class);
        Sql generator = new SqlDeletion(meta);

        String resultQuery = generator.generateById(10);

        assertEquals(expectedQuery, resultQuery);
    }
}