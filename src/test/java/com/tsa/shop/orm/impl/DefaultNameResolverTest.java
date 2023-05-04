package com.tsa.shop.orm.impl;

import com.tsa.shop.domain.entity.Product;
import com.tsa.shop.orm.interfaces.NameResolver;
import com.tsa.shop.orm.interfaces.EntityClassMeta;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class DefaultNameResolverTest {

    private final EntityClassMeta meta = new DefaultEntityClassMeta(Product.class);
    private final NameResolver resolver = new DefaultNameResolver(meta);

    @Test
    void shouldReturnColumnsNamesAsListOfStrings() {
        List<String> expected = List.of("product_name", "product_price", "creationdate");

        List<String> result = resolver.getColumns();

        assertIterableEquals(expected, result);
    }

    @Test
    void shouldReturnIdColumnNameAsString() {
        String expected = "product_id";

        String result = resolver.getIdName();

        assertEquals(expected, result);
    }

    @Test
    void shouldReturnTableName() {
        String expected = "products";

        String result = resolver.getTableName();

        assertEquals(expected, result);
    }
}
