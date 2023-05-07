package com.tsa.shop.database.versioncontrol;

import com.tsa.shop.domain.argsparser.interfaces.PropertyReader;
import org.flywaydb.core.Flyway;

public class DefaultFlywayBridge implements FlywayBridge {
    private final PropertyReader propertyReader;

    public DefaultFlywayBridge(PropertyReader propertyReader) {
        this.propertyReader = propertyReader;
    }

    @Override
    public void migrate() {
        Flyway flyway = configure();
        flyway.migrate();
    }

    private Flyway configure() {
        return Flyway
                .configure()
                .dataSource(
                        propertyReader.getDbUrl(),
                        propertyReader.getDbUserName(),
                        propertyReader.getDbPassword())
                .load();
    }
}
