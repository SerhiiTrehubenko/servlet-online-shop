package com.tsa.shop.servlets.util;

import com.tsa.shop.servlets.enums.UriPageConnector;
import com.tsa.shop.servlets.exceptions.WebServerException;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class PageGeneratorTest {
    private final PageGenerator pageGenerator = new PageGenerator();
    @Test
    void testGetGeneratedPageAsStreamPageNameIsPresent() throws Exception {
        String expectedContent = "<title> Home page </title>";

        InputStream pageAsInputStream =
                pageGenerator.getGeneratedPageAsStream(new HashMap<>(), UriPageConnector.HOME.getHtmlPage());

        String pageAsString = new String(pageAsInputStream.readAllBytes());

        assertTrue(pageAsString.contains(expectedContent));
    }

    @Test
    void testGetGeneratedPageAsStreamPageNameIsAbsent() {
        assertThrows(WebServerException.class,
                () -> pageGenerator.getGeneratedPageAsStream(new HashMap<>(), "no-page.html"));
    }

    @Test
    void testGetGeneratedPageAsStreamPageNameIsEmpty() {
        assertThrows(WebServerException.class,
                () -> pageGenerator.getGeneratedPageAsStream(new HashMap<>(), ""));
    }

    @Test
    void testGetGeneratedPageAsStreamPageNameIsNull() {
        assertThrows(WebServerException.class,
                () -> pageGenerator.getGeneratedPageAsStream(new HashMap<>(), (String) null));
    }
}