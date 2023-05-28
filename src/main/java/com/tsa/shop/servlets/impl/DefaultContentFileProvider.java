package com.tsa.shop.servlets.impl;

import com.tsa.shop.domain.HttpStatus;
import com.tsa.shop.domain.WebServerException;
import com.tsa.shop.servlets.interfaces.ContentFileProvider;

import java.io.InputStream;
import java.util.Objects;

public class DefaultContentFileProvider implements ContentFileProvider {

    public InputStream getSourceFileAsStream(String uriFromRequest) {
        InputStream inputStreamFromFile = getClass().getResourceAsStream(uriFromRequest);
        if (Objects.isNull(inputStreamFromFile)) {
            throw new WebServerException("Provided URL: [%s] is not supported".formatted(uriFromRequest), HttpStatus.NOT_FOUND, this);
        }
        return inputStreamFromFile;
    }
}
