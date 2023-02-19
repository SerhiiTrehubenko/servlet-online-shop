package com.tsa.shop.database.util;

import com.tsa.shop.database.repo.ProductRepository;
import com.tsa.shop.domain.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ResultSetParserTest {
    private ResultSetParser<Product> helper;

    private  ResultSet resultSet;

    private ResultSetMetaData metaDataResultSet;

    @BeforeEach
    public void setUp() throws SQLException {
        helper = new ResultSetParser<>(ProductRepository.class);
        resultSet = Mockito.mock(ResultSet.class);
        metaDataResultSet = Mockito.mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaDataResultSet);
        when(metaDataResultSet.getColumnCount()).thenReturn(4);

        when(metaDataResultSet.getColumnName(1)).thenReturn("product_id");
        when(metaDataResultSet.getColumnClassName(1)).thenReturn("java.lang.Long");

        when(metaDataResultSet.getColumnName(2)).thenReturn("product_name");
        when(metaDataResultSet.getColumnClassName(2)).thenReturn("java.lang.String");

        when(metaDataResultSet.getColumnName(3)).thenReturn("product_price");
        when(metaDataResultSet.getColumnClassName(3)).thenReturn("java.lang.Double");

        when(metaDataResultSet.getColumnName(4)).thenReturn("product_creationdate");
        when(metaDataResultSet.getColumnClassName(4)).thenReturn("java.sql.Date");
    }

    @Test
    void getEntityClass() {
        Class<?> expectedClass = Product.class;

        Class<?> resultClass = helper.getEntityClass();

        assertEquals(expectedClass, resultClass);
    }

    @Test
    void shouldFillMapWithColumnNamesAndColumnTypesFromReturnedResultSet() throws SQLException {
        String expectedColumnName = "product_id";
        String expectedColumnType = "java.lang.Long";

        Map<String, String> mapMeta = helper.getRetrievedColumnNamesAndColumnTypes(resultSet);

        assertTrue(mapMeta.containsKey(expectedColumnName));
        assertEquals(expectedColumnType, mapMeta.get(expectedColumnName));
    }

    @Test
    void getMethodsForRetrievingDataFromResultSet() {
        String expectedColumnName = "product_id";
        Map<String, Method> methodMap = helper.getResultSetMethodsForRetrievingData(getMetaMap());

        assertEquals(4, methodMap.size());
        assertTrue(methodMap.containsKey(expectedColumnName));
    }

    private Map<String, String> getMetaMap() {
        Map<String, String> mapMeta = new HashMap<>();

        mapMeta.put("product_id", "java.lang.Long");
        mapMeta.put("product_name", "java.lang.String");
        mapMeta.put("product_price", "java.lang.Double");
        mapMeta.put("product_creationdate", "java.sql.Date");
        return mapMeta;
    }
}