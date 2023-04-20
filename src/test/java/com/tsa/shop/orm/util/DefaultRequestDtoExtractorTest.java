package com.tsa.shop.orm.util;

import com.tsa.shop.domain.dto.ProductDto;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DefaultRequestDtoExtractorTest {

    private final DefaultRequestDtoExtractor<ProductDto> parser = new DefaultRequestDtoExtractor<>(ProductDto.class);
    @Test
    void testGetRequiredFieldsFromEntity() {
        String expectedId = "private java.lang.Long com.tsa.shop.domain.dto.ProductDto.id";
        String expectedName = "private java.lang.String com.tsa.shop.domain.dto.ProductDto.name";
        String expectedPrice = "private double com.tsa.shop.domain.dto.ProductDto.price";
        String expectedDate = "private java.sql.Date com.tsa.shop.domain.dto.ProductDto.date";

        parser.resolveFields(getParameters());
        List<Field> fieldsResult = parser.getFields();

        assertEquals(expectedId, fieldsResult.get(0).toString());
        assertEquals(expectedName, fieldsResult.get(1).toString());
        assertEquals(expectedPrice, fieldsResult.get(2).toString());
        assertEquals(expectedDate, fieldsResult.get(3).toString());
    }

    @Test
    void testGetValuesForInstance() {
        Map<String, String[]> parameters = getParameters();

        parser.resolveFields(parameters);
        parser.resolveValues(parameters);

        List<String> values = parser.getValues();

        assertEquals(parameters.get("id")[0], values.get(0));
        assertEquals(parameters.get("name")[0], values.get(1));
        assertEquals(parameters.get("price")[0], values.get(2));
        assertEquals(parameters.get("date")[0], values.get(3));

    }

    @Test
    void testConvertValue() {
        Map<String, String[]> parameters = getParameters();
        parser.resolveFields(parameters);
        parser.resolveValues(parameters);

        List<Field> fields = parser.getFields();
        List<String> values = parser.getValues();

        assertEquals(Long.class, parser.convertValue(fields.get(0), values.get(0)).getClass());
        assertEquals(String.class, parser.convertValue(fields.get(1), values.get(1)).getClass());
        assertEquals(Double.class, parser.convertValue(fields.get(2), values.get(2)).getClass());
        assertEquals(Date.class, parser.convertValue(fields.get(3), values.get(3)).getClass());
    }

    @Test
    void testEnrichInstance() throws Exception {
        Map<String, String[]> parameters = getParameters();

        parser.resolveFields(parameters);
        parser.resolveValues(parameters);

        ProductDto productDto = parser.getInstance();

        assertEquals(parameters.get("id")[0], String.valueOf(productDto.getId()));
        assertEquals(parameters.get("name")[0], String.valueOf(productDto.getName()));
        assertEquals(parameters.get("price")[0], String.valueOf(productDto.getPrice()));
        assertEquals(parameters.get("date")[0], String.valueOf(productDto.getDate()));
    }

    @Test
    void testGetInstance() {
        Map<String, String[]> stringMap = getParameters();

        ProductDto productDto = parser.getDtoInstanceFromParameters(stringMap);

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