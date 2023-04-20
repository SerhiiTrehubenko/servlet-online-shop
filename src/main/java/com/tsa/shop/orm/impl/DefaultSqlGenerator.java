package com.tsa.shop.orm.impl;

import com.tsa.shop.orm.interfaces.AbstractSqlFactory;
import com.tsa.shop.orm.interfaces.AbstractSqlGenerator;

import java.io.Serializable;

public class DefaultSqlGenerator extends AbstractSqlGenerator {

    public DefaultSqlGenerator(AbstractSqlFactory factory) {
        super(factory);
    }

    @Override
    public String findAll() {
        if (isCreated(super.selection)) {
            selection = factory.createSelector();
        }
        return selection.generate();
    }

    @Override
    public String findById(Serializable id) {
        if (isCreated(super.selection)) {
            selection = factory.createSelector();
        }
        return selection.generateById(id);
    }

    @Override
    public String add(Object entityToInsert) {
        if (isCreated(super.insertion)) {
            insertion = factory.createInsertion();
        }
        return insertion.generateByObject(entityToInsert);
    }

    @Override
    public String deleteById(Serializable id) {
        if (isCreated(super.deletion)) {
            deletion = factory.createDeletion();
        }
        return deletion.generateById(id);
    }

    @Override
    public String update(Object entityToInsert) {
        if (isCreated(update)) {
            update = factory.createUpdate();
        }
        return update.generateByObject(entityToInsert);
    }
}
