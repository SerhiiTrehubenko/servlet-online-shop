package com.tsa.shop.orm.impl;

import com.tsa.shop.orm.interfaces.NameResolver;
import com.tsa.shop.orm.interfaces.Sql;

public class SqlSelection extends Sql {

    public SqlSelection(NameResolver resolver) {
        super(resolver);
    }

    @Override
    public String generate() {
        String columns = getColumns();
        return getQuery(
                SELECT, columns,
                FROM, resolver.getTableName(),
                SEMICOLON
        );
    }

    @Override
    public String generateById() {
        String columns = getColumns();
        return getQuery(SELECT, columns, FROM, resolver.getTableName(), WHERE, resolver.getIdName(), EQUALS, PLACE_HOLDER, SEMICOLON);
    }

    private String getColumns() {
        return toFormattedString(resolver.getColumns()) + ", %s".formatted(resolver.getIdName());
    }
}
