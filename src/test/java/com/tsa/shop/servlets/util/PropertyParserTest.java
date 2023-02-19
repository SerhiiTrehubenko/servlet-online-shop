package com.tsa.shop.servlets.util;

import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class PropertyParserTest {

    PropertyParser tuner = new PropertyParser();

    @Test
    void testGetAvailablePortIsAvailableAndInRange() {
        int expectedPort = 3500;
        int availablePort = tuner.getAvailablePort(expectedPort);

        assertEquals(expectedPort, availablePort);
    }

    @Test
    void testGetAvailablePortIsBelowRange() {
        int expectedPort = 3001;
        int availablePort = tuner.getAvailablePort(-1);

        assertEquals(expectedPort, availablePort);
    }

    @Test
    void testGetAvailablePortIsAboveRange() {
        int expectedPort = 3001;
        int availablePort = tuner.getAvailablePort(70_000);

        assertEquals(expectedPort, availablePort);
    }

//    @Test
//    void testSetPort() {
//        int expectedPort = 3005;
//        int availablePort = tuner.resolvePort("3005", "hello");
//
//        assertEquals(expectedPort, availablePort);
//    }

    @Test
    void testisEligibleArgsNumber() {
        assertThrows(RuntimeException.class, () -> tuner.checkArgsNumber(new String[3]));
    }

    @Test
    void testSetProperties() {
        String expected = "shop";
        String propertyKey = "dbName";

        Properties properties = tuner.resolveProperties();

        assertEquals(expected, properties.getProperty(propertyKey));
    }

    @Test
    void testSetPropertiesCustom() {
        String expected = "shop";
        String propertyKey = "dbName";

        Properties properties = tuner.resolveProperties("F:/0_CODING/11_Anatol_java/servlet-online-shop/src/test/resources/application.properties");

        assertEquals(expected, properties.getProperty(propertyKey));
    }

    @Test
    void testStateDefender() {
        tuner.analyseIncomeArgs();
        assertThrows(RuntimeException.class, () -> tuner.analyseIncomeArgs());
    }
}