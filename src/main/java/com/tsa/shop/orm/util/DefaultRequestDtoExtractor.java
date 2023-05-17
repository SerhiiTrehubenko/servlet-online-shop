package com.tsa.shop.orm.util;

import com.tsa.shop.orm.interfaces.RequestDtoExtractor;
import com.tsa.shop.servlets.enums.HttpStatus;
import com.tsa.shop.servlets.exceptions.WebServerException;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Date;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DefaultRequestDtoExtractor<E> implements RequestDtoExtractor<E> {
    private final Class<E> classDto;
    private final static int VALUE_CELL = 0;
    private List<Field> fields;
    private List<String> values;

    public DefaultRequestDtoExtractor(Class<E> dtoClass) {
        this.classDto = dtoClass;
    }

    @Override
    public RequestDtoExtractor<E> createInstance() {
        return new DefaultRequestDtoExtractor<>(classDto);
    }

    public E getDtoInstanceFromParameters(Map<String, String[]> parameters) {
        try {
            resolveFields(parameters);
            resolveValues(parameters);

            return getInstance();
        } catch (IllegalArgumentException | WebServerException e) {
            throw e;
        } catch (Exception e) {
            throw new WebServerException("There was a problem during request processing", e, HttpStatus.INTERNAL_SERVER_ERROR, this);
        }
    }

    void resolveFields(Map<String, String[]> parameters) {
        Field[] fieldsInDto = classDto.getDeclaredFields();

        fields = Arrays.stream(fieldsInDto)
                .filter(field -> parameters.containsKey(field.getName()))
                .toList();
    }

    void resolveValues(Map<String, String[]> parameters) {
        values = fields.stream()
                .map(Field::getName)
                .filter(parameters::containsKey)
                .map(parameters::get)
                .map(array -> array[VALUE_CELL])
                .toList();
    }

    E getInstance() throws Exception {
        E instance = classDto.getConstructor().newInstance();
        Iterator<Field> fieldIterator = fields.iterator();
        Iterator<String> valuesIterator = values.iterator();

        while (fieldIterator.hasNext()) {
            Field currentField = fieldIterator.next();
            currentField.setAccessible(true);
            Serializable concertedValue = convertValue(currentField, valuesIterator.next());
            currentField.set(instance, concertedValue);
        }

        return instance;
    }

    Serializable convertValue(Field field, String value) {
        if (value.isEmpty()) {
            throw new WebServerException("Value cannot be EMPTY", HttpStatus.BAD_REQUEST, this);
        }
        Class<?> fieldType = field.getType();

        try {
            if (fieldType.isAssignableFrom(Long.class)) {
                return Long.parseLong(value);
            } else if (fieldType.isAssignableFrom(double.class)) {
                return Double.parseDouble(value);
            } else if (fieldType.isAssignableFrom(boolean.class)) {
                return Boolean.parseBoolean(value);
            } else if (fieldType.isAssignableFrom(Date.class)) {
                return Date.valueOf(value);
            } else if (fieldType.isAssignableFrom(int.class)) {
                return Integer.parseInt(value);
            }
        } catch (NumberFormatException e) {
            throw new WebServerException("Field: [%s] has wrong format".formatted(field.getName()), e, HttpStatus.BAD_REQUEST, this);
        }

        return value;
    }

    List<Field> getFields() {
        return fields;
    }

    List<String> getValues() {
        return values;
    }
}
