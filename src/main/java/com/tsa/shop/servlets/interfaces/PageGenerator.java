package com.tsa.shop.servlets.interfaces;

import com.tsa.shop.servlets.exceptions.WebServerException;

import java.io.InputStream;
import java.util.Map;

public interface PageGenerator {
    InputStream getGeneratedPageAsStream(Map<String, Object> parsedRequest, String pageName)
            throws WebServerException;
}
