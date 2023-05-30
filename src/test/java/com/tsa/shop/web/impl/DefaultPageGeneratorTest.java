package com.tsa.shop.web.impl;

import com.tsa.shop.domain.UriPageConnector;
import com.tsa.shop.domain.WebServerException;
import com.tsa.shop.web.impl.DefaultPageGenerator;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class DefaultPageGeneratorTest {
    private final DefaultPageGenerator defaultPageGenerator = new DefaultPageGenerator();
    @Test
    void testGetGeneratedPageAsStreamPageNameIsPresent() throws Exception {
        String expectedContent = "<title> Home page </title>";

        InputStream pageAsInputStream =
                defaultPageGenerator.getGeneratedPageAsStream(new HashMap<>(), UriPageConnector.HOME.getHtmlPage());

        String pageAsString = new String(pageAsInputStream.readAllBytes());

        assertTrue(pageAsString.contains(expectedContent));
    }

    @Test
    void testGetGeneratedPageAsStreamPageNameIsAbsent() {
        assertThrows(WebServerException.class,
                () -> defaultPageGenerator.getGeneratedPageAsStream(new HashMap<>(), "no-page.html"));
    }

    @Test
    void testGetGeneratedPageAsStreamPageNameIsEmpty() {
        assertThrows(java.lang.IllegalArgumentException.class,
                () -> defaultPageGenerator.getGeneratedPageAsStream(new HashMap<>(), ""));
    }

    @Test
    void testGetGeneratedPageAsStreamPageNameIsNull() {
        assertThrows(java.lang.IllegalArgumentException.class,
                () -> defaultPageGenerator.getGeneratedPageAsStream(new HashMap<>(), null));
    }
}