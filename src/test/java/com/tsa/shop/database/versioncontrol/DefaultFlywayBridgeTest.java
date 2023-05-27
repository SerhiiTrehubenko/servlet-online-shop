package com.tsa.shop.database.versioncontrol;

import com.tsa.shop.argsparser.interfaces.PropertyReader;
import com.tsa.shop.flyway.DefaultFlywayBridge;
import com.tsa.shop.flyway.FlywayBridge;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class DefaultFlywayBridgeTest {

    private final static String DB_URL = "jdbc:postgresql://localhost:5432/shop";
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
