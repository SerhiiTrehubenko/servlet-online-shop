package com.tsa.shop.web.interfaces;

import java.io.InputStream;
import java.util.Map;

public interface PageGenerator {
    InputStream getGeneratedPageAsStream(Map<String, Object> parsedRequest, String pageName);
    InputStream getGeneratedPageAsStream(String pageName);
}
