package com.tsa.shop.servlets.util;

import com.tsa.shop.servlets.enums.HttpStatus;
import com.tsa.shop.servlets.enums.UriPageConnector;
import com.tsa.shop.servlets.exceptions.WebServerException;

import java.io.InputStream;
import java.util.Objects;

public class SourceProvider {

    private final static String TEMPLATES = "/templates";

    //TODO why does "forward slash" work? Paths.get() does not work!!!
    public static InputStream getPageContentAsStream(UriPageConnector pageConnector) {
        String pageFileName = "/" + pageConnector.getHtmlPage();
        String pathToPageFile = TEMPLATES + pageFileName;
        return SourceProvider.class.getResourceAsStream(pathToPageFile);
    }

    public static InputStream getSourceFileContent(String uriFromRequest) throws WebServerException {
        InputStream inputStreamFromFile = SourceProvider.class.getResourceAsStream(uriFromRequest);
        if (Objects.isNull(inputStreamFromFile)){
            throw new WebServerException("A source file is not found", HttpStatus.NOT_FOUND);
        }
        return inputStreamFromFile;
    }
}
