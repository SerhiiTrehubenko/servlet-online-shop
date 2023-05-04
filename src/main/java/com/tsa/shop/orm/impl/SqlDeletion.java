package com.tsa.shop.orm.impl;

import com.tsa.shop.orm.interfaces.NameResolver;
import com.tsa.shop.orm.interfaces.Sql;

public class SqlDeletion extends Sql {
    protected static final String DELETE = "DELETE";

    protected SqlDeletion(NameResolver resolver) {
        super(resolver);
    }

    @Override
    public String generateById() {
        return getQuery(
                DELETE,
                FROM, resolver.getTableName(),
                WHERE, resolver.getIdName(), EQUALS, PLACE_HOLDER,
                SEMICOLON
        );
    }
}
