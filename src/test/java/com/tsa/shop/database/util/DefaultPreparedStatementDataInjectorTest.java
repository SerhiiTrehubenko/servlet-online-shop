package com.tsa.shop.database.util;

import com.tsa.shop.database.interfaces.PreparedStatementDataInjector;
import com.tsa.shop.domain.entity.Product;
import com.tsa.shop.orm.impl.DefaultEntityClassMeta;
import com.tsa.shop.orm.interfaces.EntityClassMeta;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultPreparedStatementDataInjectorTest {

    private final PreparedStatement statement = mock(PreparedStatement.class);
    private final ResultSetMetaData metaData = mock(ResultSetMetaData.class);

    @Test
    void shouldReturnValidPreparedStatement() throws SQLException {

        when(statement.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(3);
        EntityClassMeta meta = new DefaultEntityClassMeta(Product.class);

        PreparedStatementDataInjector<Product> generator = new DefaultPreparedStatementDataInjector<>(meta);

        PreparedStatement preparedStatement = generator.injectColumns(statement, new Product(1L, "hello", 25, java.sql.Date.valueOf("2023-01-23")));

        assertEquals(3, preparedStatement.getMetaData().getColumnCount());
    }
}
