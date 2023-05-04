package com.tsa.shop.database.interfaces;

import java.io.Serializable;
import java.sql.PreparedStatement;

public interface PreparedStatementDataInjector<T> {
    PreparedStatement injectColumns(PreparedStatement statement, T product);
    PreparedStatement injectId(PreparedStatement statement, Serializable resolvedId);
    PreparedStatement injectColumnsAndId(PreparedStatement statement, T entity);
}
