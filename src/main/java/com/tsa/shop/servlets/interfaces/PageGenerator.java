package com.tsa.shop.servlets.interfaces;

import java.io.InputStream;
import java.util.Map;

public interface PageGenerator {
    InputStream getGeneratedPageAsStream(Map<String, Object> parsedRequest, String pageName);
    InputStream getGeneratedPageAsStream(String pageName);
}
