package com.tsa.shop.database.util;

import com.tsa.shop.database.interfaces.EntityRowFetcher;
import com.tsa.shop.database.interfaces.RowDataExtractor;
import com.tsa.shop.orm.interfaces.EntityClassMeta;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.Map;

public class DefaultEntityRowFetcher<T> implements EntityRowFetcher<T> {
    private final EntityClassMeta meta;
    private final RowDataExtractor rowDataExtractor;

    public DefaultEntityRowFetcher(EntityClassMeta meta,
                                   RowDataExtractor rowDataExtractor) {
        this.meta = meta;
        this.rowDataExtractor = rowDataExtractor;
    }

    @SuppressWarnings("unchecked")
    public Class<T> getEntityClass() {
        return (Class<T>) meta.getClassToParse();
    }

    public T getEntityFrom(ResultSet resultSetWithData) {
        try {
            Map<Field, Serializable> rowValues = rowDataExtractor.getValues(resultSetWithData);

            return getEntity(rowValues);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("There was a problem during building an Entity: [%s]".formatted(getEntityClass().getName()),e);
        }
    }

    private T getEntity(Map<Field, Serializable> rowValues) throws Exception {
        T entityInstance = createEntityInstance();

        rowValues.forEach((column, value) -> {
            column.setAccessible(true);
            try {
                column.set(entityInstance, value);
            } catch (Exception e) {
                throw new RuntimeException("There was a problem during setting value to the entity: [%s], field: [%s]".formatted(entityInstance.getClass().getName(), column.getName()), e);
            }
        });

        return entityInstance;
    }

    private T createEntityInstance() throws Exception {
        return getEntityClass().getConstructor().newInstance();
    }
}
