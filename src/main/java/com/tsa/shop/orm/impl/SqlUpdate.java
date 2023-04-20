package com.tsa.shop.orm.impl;

import com.tsa.shop.orm.interfaces.EntityClassMeta;
import com.tsa.shop.orm.interfaces.Sql;

import java.util.Iterator;
import java.util.List;
import java.util.StringJoiner;

public class SqlUpdate extends Sql {

    private final static String UPDATE = "UPDATE ";
    private final static String SET = " SET ";


    public SqlUpdate(EntityClassMeta meta) {
        super(meta);
    }

    @Override
    public String generateByObject(Object objectToInsert) {
        return getQuery(
                UPDATE, meta.getTableName(),
                SET, getColumnValueString(objectToInsert),
                WHERE, meta.getIdColumnName(), EQUALS, meta.getId(objectToInsert),
                SEMICOLON
        );
    }

    private String getColumnValueString(Object objectToInsert) {
        List<String> columnsNames = meta.getColumnsNames();
        List<String> columnsValues = meta.getColumnsValues(objectToInsert);

        return joinToString(columnsNames.iterator(), columnsValues.iterator());
    }

    private String joinToString(Iterator<String> namesIterator, Iterator<String> valuesIterator) {
        StringJoiner joiner = new StringJoiner(COMA_WHITESPACE_DELIMITER);

        while (namesIterator.hasNext()) {
            joiner.add("%s%s%s".formatted(namesIterator.next(), EQUALS, valuesIterator.next()));
        }

        return joiner.toString();
    }
}
