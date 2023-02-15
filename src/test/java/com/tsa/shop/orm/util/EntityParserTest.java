package com.tsa.shop.orm.util;

import com.tsa.shop.domain.dto.ProductDto;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EntityParserTest {

    @Test
    void testGetRequiredFieldsFromEntity() {
        String expectedId = "private java.lang.Long com.tsa.shop.domain.dto.ProductDto.id";
        String expectedName = "private java.lang.String com.tsa.shop.domain.dto.ProductDto.name";
        String expectedPrice = "private double com.tsa.shop.domain.dto.ProductDto.price";
        String expectedDate = "private java.sql.Date com.tsa.shop.domain.dto.ProductDto.date";


        Field[] fieldsResult = EntityParser.getRequiredFieldsFromEntity(ProductDto.class, getParameters());

        assertEquals(expectedId, fieldsResult[0].toString());
        assertEquals(expectedName, fieldsResult[1].toString());
        assertEquals(expectedPrice, fieldsResult[2].toString());
        assertEquals(expectedDate, fieldsResult[3].toString());
    }

    @Test
    void testGetValuesForInstance() {
        Map<String, String[]> parameters = getParameters();

        Field[] fields = EntityParser.getRequiredFieldsFromEntity(ProductDto.class, parameters);

        Object[] values = EntityParser.getValuesForInstance(fields, parameters);

        assertEquals(parameters.get("id")[0], String.valueOf(values[0]));
        assertEquals(parameters.get("name")[0], String.valueOf(values[1]));
        assertEquals(parameters.get("price")[0], String.valueOf(values[2]));
        assertEquals(parameters.get("date")[0], String.valueOf(values[3]));

    }

    @Test
    void testConvertValue() {
        Map<String, String[]> parameters = getParameters();

        Field[] fields = EntityParser.getRequiredFieldsFromEntity(ProductDto.class, parameters);

        Object[] values = EntityParser.getValuesForInstance(fields, parameters);

        assertEquals(Long.class, EntityParser.convertValue(fields[0], values[0]).getClass());
        assertEquals(String.class, EntityParser.convertValue(fields[1], values[1]).getClass());
        assertEquals(Double.class, EntityParser.convertValue(fields[2], values[2]).getClass());
        assertEquals(Date.class, EntityParser.convertValue(fields[3], values[3]).getClass());
    }

    @Test
    void testEnrichInstance() throws Exception {
        Map<String, String[]> parameters = getParameters();

        Field[] fields = EntityParser.getRequiredFieldsFromEntity(ProductDto.class, parameters);

        Object[] values = EntityParser.getValuesForInstance(fields, parameters);

        ProductDto productDto = ProductDto.class.getConstructor().newInstance();

        EntityParser.enrichInstance(fields, values, productDto);

        assertEquals(parameters.get("id")[0], String.valueOf(productDto.getId()));
        assertEquals(parameters.get("name")[0], String.valueOf(productDto.getName()));
        assertEquals(parameters.get("price")[0], String.valueOf(productDto.getPrice()));
        assertEquals(parameters.get("date")[0], String.valueOf(productDto.getDate()));
    }

    @Test
    void testGetInstance() {
        Map<String, String[]> stringMap = getParameters();

        ProductDto productDto = EntityParser.getDtoInstance(ProductDto.class, stringMap);

        assertNotNull(productDto);
        assertEquals(15, productDto.getId());
        assertEquals("comp", productDto.getName());
    }

    private Map<String, String[]> getParameters() {
        return  Map.of("id", new String[]{"15"},
                "name", new String[]{"comp"}, "price", new String[]{"15.5"},
                "date", new String[]{"2023-01-11"});
    }
}