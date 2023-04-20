package com.tsa.shop.database.util;

import com.tsa.shop.domain.entity.Product;
import com.tsa.shop.orm.impl.DefaultEntityClassMeta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class DefaultResultSetParserTest {
    private DefaultResultSetParser<Product> helper;

    private ResultSet resultSet;

    @BeforeEach
    public void setUp() throws SQLException {
        helper = new DefaultResultSetParser<>(new DefaultEntityClassMeta(Product.class));
        resultSet = Mockito.mock(ResultSet.class);
        ResultSetMetaData metaDataResultSet = Mockito.mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaDataResultSet);
        when(metaDataResultSet.getColumnCount()).thenReturn(4);

        when(metaDataResultSet.getColumnName(1)).thenReturn("product_id");
        when(metaDataResultSet.getColumnClassName(1)).thenReturn("java.lang.Long");

        when(metaDataResultSet.getColumnName(2)).thenReturn("product_name");
        when(metaDataResultSet.getColumnClassName(2)).thenReturn("java.lang.String");

        when(metaDataResultSet.getColumnName(3)).thenReturn("product_price");
        when(metaDataResultSet.getColumnClassName(3)).thenReturn("java.lang.Double");

        when(metaDataResultSet.getColumnName(4)).thenReturn("creationdate");
        when(metaDataResultSet.getColumnClassName(4)).thenReturn("java.sql.Date");

        when(resultSet.getLong("product_id")).thenReturn(10L);
        when(resultSet.getString("product_name")).thenReturn("comp 10");
        when(resultSet.getDouble("product_price")).thenReturn(505.55);
        when(resultSet.getDate("creationdate")).thenReturn(Date.valueOf("2023-06-05"));
    }

    @Test
    void getEntityClass() {
        Class<?> expectedClass = Product.class;

        Class<?> resultClass = helper.getEntityClass();

        assertEquals(expectedClass, resultClass);
    }

    @Test
    void shouldFillMapWithColumnNamesAndColumnTypesFromReturnedResultSet() throws Exception {
        String expectedColumnName = "product_id";
        String expectedColumnType = "java.lang.Long";

        Map<String, String> mapMeta = helper.getColumnNamesAndTypes(resultSet);

        assertTrue(mapMeta.containsKey(expectedColumnName));
        assertEquals(expectedColumnType, mapMeta.get(expectedColumnName));
    }

    @Test
    void getMethodsForRetrievingDataFromResultSet() throws Exception {
        String expectedColumnName = "product_id";
        Map<String, Method> methodMap = helper.getResultSetMethodsForFetchingData(resultSet);

        assertEquals(4, methodMap.size());
        assertTrue(methodMap.containsKey(expectedColumnName));
    }

    @Test
    void name() throws Exception {
        String expected = "Product{id=10, name='comp 10', price=505.55, date=2023-06-05}";

        Product result = helper.getEntityFrom(resultSet);

        assertEquals(expected, result.toString());
    }
}