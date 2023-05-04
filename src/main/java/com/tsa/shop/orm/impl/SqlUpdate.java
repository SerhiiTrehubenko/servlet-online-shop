package com.tsa.shop.orm.impl;

import com.tsa.shop.orm.interfaces.NameResolver;
import com.tsa.shop.orm.interfaces.Sql;

import java.util.Iterator;
import java.util.List;
import java.util.StringJoiner;

public class SqlUpdate extends Sql {

    private final static String UPDATE = "UPDATE ";
    private final static String SET = " SET ";


    public SqlUpdate(NameResolver resolver) {
        super(resolver);
    }

    @Override
    public String generateByObject(Object objectToInsert) {
        validateObject(objectToInsert);

        return getQuery(
                UPDATE, resolver.getTableName(),
                SET, getColumns(),
                WHERE, resolver.getIdName(), EQUALS, PLACE_HOLDER,
                SEMICOLON
        );
    }

    private String getColumns() {
        List<String> columnsNames = resolver.getColumns();

        return joinToString(columnsNames.iterator());
    }

    private String joinToString(Iterator<String> namesIterator) {
        StringJoiner joiner = new StringJoiner(COMA_WHITESPACE_DELIMITER);

        while (namesIterator.hasNext()) {
            joiner.add("%s%s%s".formatted(namesIterator.next(), EQUALS, PLACE_HOLDER));
        }

        return joiner.toString();
    }
}
