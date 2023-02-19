package com.tsa.shop.servlets.util;

import com.tsa.shop.servlets.enums.HttpStatus;
import com.tsa.shop.servlets.exceptions.WebServerException;

import java.io.InputStream;
import java.util.Objects;

public class SourceFilesProvider {

    public InputStream getSourceFileAsStream(String uriFromRequest) throws WebServerException {
        InputStream inputStreamFromFile = getClass().getResourceAsStream(uriFromRequest);
        if (Objects.isNull(inputStreamFromFile)) {
            throw new WebServerException("A source file is not found", HttpStatus.NOT_FOUND);
        }
        return inputStreamFromFile;
    }
}
