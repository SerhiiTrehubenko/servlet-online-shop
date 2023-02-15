package com.tsa.shop.database.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyParser {

    public static Properties getProperties() {
        try (var input = PropertyParser.class
                .getClassLoader()
                .getResourceAsStream("application.properties")) {
            Properties properties = new Properties();
            properties.load(input);
            return properties;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static Properties getCustomProperties(String path) throws IOException {
        try (var input = new FileInputStream(String.valueOf(path))) {
            Properties properties = new Properties();
            properties.load(input);
            return properties;
        }
    }
}