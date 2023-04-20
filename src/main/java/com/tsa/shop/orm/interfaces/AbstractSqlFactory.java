package com.tsa.shop.orm.interfaces;

public abstract class AbstractSqlFactory {

    protected final EntityClassMeta meta;

    protected AbstractSqlFactory(EntityClassMeta meta) {
        this.meta = meta;
    }

    public abstract Sql createSelector();

    public abstract Sql createDeletion();

    public abstract Sql createInsertion();

    public abstract Sql createUpdate();
}
