package com.tsa.shop.orm.interfaces;

public abstract class AbstractSqlFactory {

    protected final NameResolver resolver;

    protected AbstractSqlFactory(NameResolver resolver) {
        this.resolver = resolver;
    }

    public abstract Sql createSelector();

    public abstract Sql createDeletion();

    public abstract Sql createInsertion();

    public abstract Sql createUpdate();
}
