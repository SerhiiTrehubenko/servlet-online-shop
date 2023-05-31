package com.tsa.shop.flyway;

import com.tsa.shop.argsparser.interfaces.PropertyReader;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class DefaultFlywayBridgeITest {

    private final static String DB_URL = "jdbc:postgresql://localhost:5433/shop";
    private final static String DB_USER_NAME = "postgres";
    private final static String DB_PASSWORD = "password";

    @Test
    void shouldCallMethodsForUrlUsernameAndPasswordOnlyOnce() {
        PropertyReader propertyReader = mock(PropertyReader.class);
        when(propertyReader.getDbUrl()).thenReturn(DB_URL);
        when(propertyReader.getDbUserName()).thenReturn(DB_USER_NAME);
        when(propertyReader.getDbPassword()).thenReturn(DB_PASSWORD);

        FlywayBridge flywayBridge = new DefaultFlywayBridge(propertyReader);
        flywayBridge.migrate();

        verify(propertyReader).getDbUrl();
        verify(propertyReader).getDbUserName();
        verify(propertyReader).getDbPassword();
    }
}
