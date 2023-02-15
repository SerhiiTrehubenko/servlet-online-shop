package com.tsa.shop.servlets.util;

import com.tsa.shop.servlets.enums.UriPageConnector;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class SourceProviderTest {

    @Test
    void testGetPageSourceAsString() {
        String expexted = "<@layout.head title = \"Home page\"/>";
        try (InputStream inputStream = SourceProvider.getPageContentAsStream(UriPageConnector.HOME)){
            assertNotNull(inputStream);

            String pageContent = new String(inputStream.readAllBytes());

            assertTrue(pageContent.contains(expexted));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}