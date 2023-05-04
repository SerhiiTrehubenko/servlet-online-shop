package com.tsa.shop.orm.impl;

import com.tsa.shop.domain.entity.Product;
import com.tsa.shop.orm.interfaces.NameResolver;
import com.tsa.shop.orm.interfaces.Sql;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SqlUpdateTest {

    private final DefaultEntityClassMeta meta = new DefaultEntityClassMeta(Product.class);
    private final NameResolver resolver = new DefaultNameResolver(meta);
    private final Sql update = new SqlUpdate(resolver);

    @Test
    void shouldReturnQueryForUpdate() {
        String expectedQuery = "UPDATE products SET product_name=?, product_price=?, creationdate=? WHERE product_id=?;";

        Product product = SqlInsertionTest.createProduct();
        String resultQuery = update.generateByObject(product);

        assertEquals(expectedQuery, resultQuery);
    }
}
