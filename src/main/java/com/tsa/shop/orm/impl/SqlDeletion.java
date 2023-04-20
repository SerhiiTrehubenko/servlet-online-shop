package com.tsa.shop.orm.impl;

import com.tsa.shop.orm.interfaces.EntityClassMeta;
import com.tsa.shop.orm.interfaces.Sql;

import java.io.Serializable;
import java.util.Objects;

public class SqlDeletion extends Sql {
    protected static final String DELETE = "DELETE";

    protected SqlDeletion(EntityClassMeta meta) {
        super(meta);
    }

    @Override
    public String generateById(Serializable id) {
        Objects.requireNonNull(id);
        return getQuery(DELETE, FROM, meta.getTableName(), WHERE, meta.getIdColumnName(), EQUALS, resolveId(id), SEMICOLON);
    }
}
