package com.tsa.shop.database.repo;

import com.tsa.shop.database.interfaces.DbConnector;
import com.tsa.shop.database.interfaces.IdResolver;
import com.tsa.shop.database.interfaces.PreparedStatementDataInjector;
import com.tsa.shop.database.interfaces.EntityRowFetcher;
import com.tsa.shop.orm.interfaces.AbstractSqlGenerator;

public class ProductRepository<T> extends AbstractTsaRepository<T> {
    public ProductRepository(AbstractSqlGenerator queryGenerator,
                             EntityRowFetcher<T> entityRowFetcher,
                             DbConnector connector,
                             PreparedStatementDataInjector<T> preparedStatementDataInjector,
                             IdResolver idResolver) {
        super(queryGenerator, entityRowFetcher, connector, preparedStatementDataInjector, idResolver);
    }
}
