package com.tsa.shop.orm.impl;

import com.tsa.shop.orm.interfaces.EntityClassMeta;
import com.tsa.shop.orm.interfaces.Sql;

import java.util.List;
import java.util.Objects;

public class SqlInsertion extends Sql {

    private final static String INSERT = "INSERT ";
    private final static String INTO = "INTO ";
    private final static String VALUES = " VALUES";
    private final static String OPEN_BRACKET = " (";
    private final static String CLOSE_BRACKET = ")";

    protected SqlInsertion(EntityClassMeta meta) {
        super(meta);
    }

    @Override
    public String generateByObject(Object entityToInsert) {
        requiredObjectComplyParsedClass(entityToInsert);
        String tableName = meta.getTableName();

        return getQuery(INSERT, INTO, tableName, getColumnsNamesToInsert(), VALUES, getValuesToInsert(entityToInsert), SEMICOLON);
    }

    private void requiredObjectComplyParsedClass(Object entityToInsert) {
        Objects.requireNonNull(entityToInsert, String.format("During initializing %s, provided Object for insertion was null", SqlInsertion.class.getName()));
        Class<?> parsedClass = meta.getClassToParse();
        Class<?> entityClass = entityToInsert.getClass();
        if (!parsedClass.isAssignableFrom(entityClass)) {
            throw new RuntimeException(String.format("Provided Instance: [%s] does not comply to parsed class: [%s]",
                    entityToInsert.getClass().getName(), parsedClass.getName()));
        }
    }

    private String getColumnsNamesToInsert() {
        List<String> columns = meta.getColumnsNames();
        return toFormatString(columns);
    }

    private String getValuesToInsert(Object entityToInsert) {
        List<String> values = meta.getColumnsValues(entityToInsert);
        return toFormatString(values);
    }

    @Override
    protected String toFormatString(List<String> arguments) {
        String formatted = super.toFormatString(arguments);
        return addBracketsBothSides(formatted);
    }

    private String addBracketsBothSides(String value) {
        return OPEN_BRACKET + value + CLOSE_BRACKET;
    }
}
