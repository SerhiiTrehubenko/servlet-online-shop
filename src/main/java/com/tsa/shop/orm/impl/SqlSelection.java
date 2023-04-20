package com.tsa.shop.orm.impl;

import com.tsa.shop.orm.interfaces.EntityClassMeta;
import com.tsa.shop.orm.interfaces.Sql;

import java.io.Serializable;
import java.util.Objects;

public class SqlSelection extends Sql {

    public SqlSelection(EntityClassMeta meta) {
        super(meta);
    }

    @Override
    public String generate() {
        String columns = getColumns();
        return getQuery(SELECT, columns, FROM, meta.getTableName(), SEMICOLON);
    }

    @Override
    public String generateById(Serializable id) {
        Objects.requireNonNull(id);
        String columns = getColumns();
        return getQuery(SELECT, columns, FROM, meta.getTableName(), WHERE, meta.getIdColumnName(), EQUALS, resolveId(id), SEMICOLON);
    }

    private String getColumns() {
        return toFormatString(meta.getColumnsNames()) + ", %s".formatted(meta.getIdColumnName());
    }
}
