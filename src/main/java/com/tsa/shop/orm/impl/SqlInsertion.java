package com.tsa.shop.orm.impl;

import com.tsa.shop.orm.interfaces.NameResolver;
import com.tsa.shop.orm.interfaces.Sql;

import java.util.List;
import java.util.stream.Stream;

public class SqlInsertion extends Sql {

    private final static String INSERT = "INSERT ";
    private final static String INTO = "INTO ";
    private final static String VALUES = " VALUES";
    private final static String OPEN_BRACKET = " (";
    private final static String CLOSE_BRACKET = ")";

    protected SqlInsertion(NameResolver resolver) {
        super(resolver);
    }

    @Override
    public String generateByObject(Object entityToInsert) {
        validateObject(entityToInsert);

        return getQuery(
                INSERT, INTO, resolver.getTableName(),
                getColumnsPart(),
                VALUES, getValuesPart(),
                SEMICOLON
        );
    }

    private String getColumnsPart() {
        List<String> columns = resolver.getColumns();
        return toFormattedString(columns);
    }

    private String getValuesPart() {
        List<String> columns = resolver.getColumns();
        List<String> placeholders = Stream.generate(() -> PLACE_HOLDER).limit(columns.size()).toList();
        return toFormattedString(placeholders);
    }

    @Override
    protected String toFormattedString(List<String> arguments) {
        String formatted = super.toFormattedString(arguments);
        return addBracketsBothSides(formatted);
    }

    private String addBracketsBothSides(String value) {
        return OPEN_BRACKET + value + CLOSE_BRACKET;
    }
}
