package com.tsa.shop.servlets.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyReader {

    public Properties getProperties() {
        try (var input = PropertyReader.class
                .getClassLoader()
                .getResourceAsStream("application.properties")) {
            Properties properties = new Properties();
            properties.load(input);
            return properties;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public Properties getCustomProperties(String path) throws IOException {
        try (var input = new FileInputStream(String.valueOf(path))) {
            Properties properties = new Properties();
            properties.load(input);
            return properties;
        }
    }
}