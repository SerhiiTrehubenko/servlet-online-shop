package com.tsa.shop.orm.interfaces;

import java.io.Serializable;
import java.util.Objects;

public abstract class AbstractSqlGenerator {
    protected Sql selection;
    protected Sql insertion;
    protected Sql update;
    protected Sql deletion;
    protected final AbstractSqlFactory factory;

    protected AbstractSqlGenerator(AbstractSqlFactory factory) {
        this.factory = factory;
    }

    public abstract String findAll();
    public abstract String findById(Serializable id);
    public abstract String add(Object entityToInsert);
    public abstract String deleteById(Serializable id);
    public abstract String update(Object entityToInsert);

    protected boolean isCreated(Sql generator) {
        return !Objects.nonNull(generator);
    }
}
