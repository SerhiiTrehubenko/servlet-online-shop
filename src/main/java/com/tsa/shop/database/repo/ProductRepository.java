package com.tsa.shop.database.repo;

import com.tsa.shop.database.interfaces.DbConnector;
import com.tsa.shop.database.interfaces.ResultSetParser;
import com.tsa.shop.orm.interfaces.AbstractSqlGenerator;

public class ProductRepository<T> extends AbstractTsaRepository<T> {
    public ProductRepository(AbstractSqlGenerator queryGenerator,
                             ResultSetParser<T> defaultResultSetParser,
                             DbConnector connector) {
        super(queryGenerator, defaultResultSetParser, connector);
    }
}
