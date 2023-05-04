package com.tsa.shop.database.util;

import com.tsa.shop.database.interfaces.ResultSetMethodProvider;
import com.tsa.shop.database.interfaces.RowDataExtractor;
import com.tsa.shop.domain.entity.Product;
import com.tsa.shop.orm.impl.DefaultEntityClassMeta;
import com.tsa.shop.orm.interfaces.EntityClassMeta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class DefaultEntityRowFetcherTest {
    private DefaultEntityRowFetcher<Product> entityFetcher;

    private ResultSet resultSet;

    @BeforeEach
    public void setUp() throws SQLException {
        EntityClassMeta meta = new DefaultEntityClassMeta(Product.class);
        ResultSetMethodProvider methodProvider = new ResultSetMethodProviderImpl();
        RowDataExtractor extractor = new RowDataExtractorImpl(meta, methodProvider);
        entityFetcher = new DefaultEntityRowFetcher<>(meta, extractor);
        resultSet = Mockito.mock(ResultSet.class);

        when(resultSet.getLong("product_id")).thenReturn(10L);
        when(resultSet.getString("product_name")).thenReturn("comp 10");
        when(resultSet.getDouble("product_price")).thenReturn(505.55);
        when(resultSet.getDate("creationdate")).thenReturn(Date.valueOf("2023-06-05"));
    }

    @Test
    void getEntityClass() {
        Class<?> expectedClass = Product.class;

        Class<?> resultClass = entityFetcher.getEntityClass();

        assertEquals(expectedClass, resultClass);
    }

    @Test
    void shouldReturnValidEntityFromRow() {
        String expected = "Product{id=10, name='comp 10', price=505.55, date=2023-06-05}";

        Product result = entityFetcher.getEntityFrom(resultSet);

        assertEquals(expected, result.toString());
    }
}