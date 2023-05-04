package com.tsa.shop.database.util;

import com.tsa.shop.database.interfaces.ResultSetMethodProvider;
import com.tsa.shop.database.interfaces.RowDataExtractor;
import com.tsa.shop.orm.interfaces.EntityClassMeta;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RowDataExtractorImpl implements RowDataExtractor {
    private final EntityClassMeta meta;
    private final ResultSetMethodProvider resultSetMethodProvider;

    public RowDataExtractorImpl(EntityClassMeta meta,
                                ResultSetMethodProvider resultSetMethodProvider) {
        this.meta = meta;
        this.resultSetMethodProvider = resultSetMethodProvider;
    }

    @Override
    public Map<Field, Serializable> getValues(ResultSet resultSet) {
        Set<Map.Entry<String, Field>> entityFields = meta.getAllFields();

        return entityFields.stream()
                .map(entry -> new FieldValue(getField(entry), getValue(resultSet, entry)))
                .collect(Collectors.toMap(FieldValue::field, FieldValue::value));
    }
    private Field getField(Map.Entry<String, Field> entry) {
        return entry.getValue();
    }

    private Serializable getValue(ResultSet resultSet,
                                  Map.Entry<String, Field> entry) {
        try {
            String columnName = entry.getKey();
            Field entityField = getField(entry);
            Class<?> fieldType = entityField.getType();

            var method = resultSetMethodProvider.getMethod(fieldType);

            return method.apply(resultSet, columnName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    record FieldValue(Field field, Serializable value) {
    }
}
