package com.tsa.shop.servlets.util;

import com.tsa.shop.servlets.enums.HttpStatus;
import com.tsa.shop.servlets.exceptions.WebServerException;
import com.tsa.shop.servlets.interfaces.ContentFileProvider;

import java.io.InputStream;
import java.util.Objects;

public class DefaultContentFileProvider implements ContentFileProvider {

    public InputStream getSourceFileAsStream(String uriFromRequest) throws WebServerException {
        InputStream inputStreamFromFile = getClass().getResourceAsStream(uriFromRequest);
        if (Objects.isNull(inputStreamFromFile)) {
            throw new WebServerException("Provided URL: [%s] is not supported".formatted(uriFromRequest), HttpStatus.NOT_FOUND);
        }
        return inputStreamFromFile;
    }
}
