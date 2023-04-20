package com.tsa.shop.orm.impl;

import com.tsa.shop.orm.interfaces.AbstractSqlFactory;
import com.tsa.shop.orm.interfaces.EntityClassMeta;
import com.tsa.shop.orm.interfaces.Sql;

public class DefaultSqlFactory extends AbstractSqlFactory {

    public DefaultSqlFactory(EntityClassMeta meta) {
        super(meta);
    }

    @Override
    public Sql createSelector() {
        return new SqlSelection(meta);
    }

    @Override
    public Sql createDeletion() {
        return new SqlDeletion(meta);
    }

    @Override
    public Sql createInsertion() {
        return new SqlInsertion(meta);
    }

    @Override
    public Sql createUpdate() {
        return new SqlUpdate(meta);
    }
}
