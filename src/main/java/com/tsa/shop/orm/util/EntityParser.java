package com.tsa.shop.orm.util;

import com.tsa.shop.servlets.enums.HttpStatus;
import com.tsa.shop.servlets.exceptions.WebServerException;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Date;
import java.util.Map;

public class EntityParser {

    public static <T> T getDtoInstance(Class<T> clazz, Map<String, String[]> parameters) throws WebServerException {
        try {
            Field[] requiredFieldsInEntity = getRequiredFieldsFromEntity(clazz, parameters);
            Object[] valuesForInstance = getValuesForInstance(requiredFieldsInEntity, parameters);

            T instance = clazz.getConstructor().newInstance();

            enrichInstance(requiredFieldsInEntity, valuesForInstance, instance);

            return instance;
        } catch (Exception e) {
            throw new WebServerException(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    static <T> Field[] getRequiredFieldsFromEntity(Class<T> clazz, Map<String, String[]> parameters) {
        Field[] requiredFields = new Field[parameters.size()];

        Field[] fieldsInEntity = clazz.getDeclaredFields();

        int count = 0;
        for (Field field : fieldsInEntity) {
            if (parameters.containsKey(field.getName())) {
                requiredFields[count++] = field;
            }
        }
        return requiredFields;
    }

    static Object[] getValuesForInstance(Field[] fields, Map<String, String[]> parameters) {
        Object[] values = new Object[fields.length];
        int count = 0;
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            String value = parameters.get(fieldName)[0];
            values[count++] = value;
        }
        return values;
    }

    static <T> void enrichInstance(Field[] requiredFields, Object[] values, T instance) throws IllegalAccessException {
        int count = 0;
        for (Field field : requiredFields) {
            field.setAccessible(true);
            Serializable concertedValue = convertValue(field, values[count++]);
            field.set(instance, concertedValue);
        }
    }

    static Serializable convertValue(Field field, Object value) {
        Class<?> fieldType = field.getType();
        String valueAsString = String.valueOf(value);

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
        }else {
            return valueAsString;
        }
    }
}
