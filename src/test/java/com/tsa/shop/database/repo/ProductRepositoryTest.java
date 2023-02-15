package com.tsa.shop.database.repo;

import com.tsa.shop.domain.entity.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {

    @Mock
    private ResultSet resultSet;

    @Mock
    ResultSetMetaData metaDataResultSet;

    @InjectMocks
    ProductRepository repo = new ProductRepository();

    @Test
    void getClassOfEntity() {
        assertEquals(Product.class, repo.getClassOfEntity());
    }

    @Test
    void enrichMapOfColumnNamesAndColumnTypes() throws SQLException {
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

        Map<String, String> mapMeta = new HashMap<>();

        repo.enrichMapOfColumnNamesAndColumnTypes(resultSet, mapMeta);

        assertEquals(4, mapMeta.size());
    }

    @Test
    void getMethodsForRetrievingDataFromResult() {
        Map<String, String> mapMeta = getMetaMap();

        Map<String, Method> mapOfNeededMethods = repo.getMethodsForRetrievingDataFromResult(mapMeta);
        for (Map.Entry<String, Method> stringMethodEntry : mapOfNeededMethods.entrySet()) {
            System.out.println(stringMethodEntry);
        }
        assertEquals(4, mapOfNeededMethods.size());
    }

    private Map<String, String> getMetaMap() {
        Map<String, String> mapMeta = new HashMap<>();

        mapMeta.put("product_id", "java.lang.Long");
        mapMeta.put("product_name", "java.lang.String");
        mapMeta.put("product_price", "java.lang.Double");
        mapMeta.put("product_creationdate", "java.sql.Date");
        return mapMeta;
    }

    @Test
    void findAll() {
        List<Product> productList = repo.findAll();

        assertEquals(4, productList.size());
    }
}