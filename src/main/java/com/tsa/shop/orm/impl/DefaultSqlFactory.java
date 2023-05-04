package com.tsa.shop.orm.impl;

import com.tsa.shop.orm.interfaces.AbstractSqlFactory;
import com.tsa.shop.orm.interfaces.NameResolver;
import com.tsa.shop.orm.interfaces.Sql;

public class DefaultSqlFactory extends AbstractSqlFactory {

    public DefaultSqlFactory(NameResolver resolver) {
        super(resolver);
    }

    @Override
    public Sql createSelector() {
        return new SqlSelection(resolver);
    }

    @Override
    public Sql createDeletion() {
        return new SqlDeletion(resolver);
    }

    @Override
    public Sql createInsertion() {
        return new SqlInsertion(resolver);
    }

    @Override
    public Sql createUpdate() {
        return new SqlUpdate(resolver);
    }
}
