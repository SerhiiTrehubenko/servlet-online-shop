package com.tsa.shop.orm.impl;

import com.tsa.shop.domain.entity.Product;
import com.tsa.shop.orm.interfaces.Sql;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SqlUpdateTest {

    private final DefaultEntityClassMeta meta = new DefaultEntityClassMeta(Product.class);
    private final Sql update = new SqlUpdate(meta);

    @Test
    void shouldReturnQueryForUpdate() {
        String expectedQuery = "UPDATE products SET product_name='comp', product_price=10.25, creationdate='2023-03-31' WHERE product_id=10;";

        Product product = SqlInsertionTest.createProduct();
        String resultQuery = update.generateByObject(product);

        assertEquals(expectedQuery, resultQuery);
    }
}
