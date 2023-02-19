package com.tsa.shop.orm.util;

import com.tsa.shop.servlets.enums.HttpStatus;
import com.tsa.shop.servlets.exceptions.WebServerException;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Date;
import java.util.Map;

public class EntityParser {

    private final static int VALUE_CELL = 0;

    public <T> T getDtoInstance(Class<T> classDto, Map<String, String[]> parameters) throws WebServerException {
        try {
            Field[] fields = getFields(classDto, parameters);
            Object[] values = getValues(fields, parameters);

            T instance = classDto.getConstructor().newInstance();

            enrichInstance(fields, values, instance);

            return instance;
        } catch (Exception e) {
            throw new WebServerException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    <T> Field[] getFields(Class<T> classDto, Map<String, String[]> parameters) {
        Field[] requiredFields = new Field[parameters.size()];

        Field[] fieldsInEntity = classDto.getDeclaredFields();

        int count = 0;
        for (Field field : fieldsInEntity) {
            if (parameters.containsKey(field.getName())) {
                requiredFields[count++] = field;
            }
        }
        return requiredFields;
    }

    Object[] getValues(Field[] fields, Map<String, String[]> parameters) {
        Object[] values = new Object[fields.length];
        int count = 0;
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            String fieldValue = parameters.get(fieldName)[VALUE_CELL];
            values[count++] = fieldValue;
        }
        return values;
    }

    <T> void enrichInstance(Field[] requiredFields, Object[] values, T instance) throws IllegalAccessException {
        int count = 0;
        for (Field field : requiredFields) {
            field.setAccessible(true);
            Serializable concertedValue = convertValue(field, values[count++]);
            field.set(instance, concertedValue);
        }
    }

    Serializable convertValue(Field field, Object value) throws WebServerException {
        Class<?> fieldType = field.getType();
        String valueAsString = String.valueOf(value);

        try {
            if (fieldType.isAssignableFrom(Long.class)) {
                return Long.parseLong(valueAsString);
            } else if (fieldType.isAssignableFrom(double.class)) {
                return Double.parseDouble(valueAsString);
            } else if (fieldType.isAssignableFrom(boolean.class)) {
                return Boolean.parseBoolean(valueAsString);
            } else if (fieldType.isAssignableFrom(Date.class)) {
                return Date.valueOf(valueAsString);
            } else if (fieldType.isAssignableFrom(int.class)) {
                return Integer.parseInt(valueAsString);
            }
        } catch (NumberFormatException e) {
            throw new WebServerException("Field \"" + field.getName() + "\" has wrong format");
        }

        if (valueAsString.isEmpty()) {
            throw new WebServerException("Field \"" + field.getName() + "\" cannot be empty");
        }
        return valueAsString;
    }
}
