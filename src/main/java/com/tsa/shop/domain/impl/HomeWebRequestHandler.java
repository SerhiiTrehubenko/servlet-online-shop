package com.tsa.shop.domain.impl;

import com.tsa.shop.domain.logging.DomainLogger;
import com.tsa.shop.domain.logmessagegenerator.LogMessageGenerator;
import com.tsa.shop.domain.interfaces.WebRequestHandler;
import com.tsa.shop.servlets.interfaces.*;
import com.tsa.shop.servlets.enums.UriPageConnector;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.InputStream;
import java.util.Map;
import java.util.Objects;

public class HomeWebRequestHandler extends WebRequestHandler {
    private final ContentFileProvider contentFileProvider;

    public HomeWebRequestHandler(ServletRequestParser servletRequestParser,
                                 PageGenerator pageGenerator,
                                 ResponseWriter responseWriter,
                                 Response response,
                                 DomainLogger logger,
                                 LogMessageGenerator logMessageGenerator,
                                 ContentFileProvider contentFileProvider) {
        super(servletRequestParser, pageGenerator, responseWriter, response, logger, logMessageGenerator);
        this.contentFileProvider = contentFileProvider;
    }

    @Override
    protected void doGet(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        getTemplateMethod(servletRequest, servletResponse);
    }

    @Override
    protected InputStream handleGetRequest(Map<String, Object> parsedRequest, UriPageConnector uriPageConnector) {
        InputStream content = InputStream.nullInputStream();
        if (homePage(uriPageConnector)) {
            content = getHomePage(uriPageConnector);
        } else if (file(uriPageConnector)) {
            content = getFileContent(parsedRequest);
        }
        return content;
    }

    private boolean homePage(UriPageConnector pageConnector) {
        return UriPageConnector.HOME == pageConnector || UriPageConnector.SLASH == pageConnector;
    }

    private InputStream getHomePage(UriPageConnector pageConnector) {
        return pageGenerator.getGeneratedPageAsStream(pageConnector.getHtmlPage());
    }

    private boolean file(UriPageConnector pageConnector) {
        return Objects.isNull(pageConnector);
    }

    private InputStream getFileContent(Map<String, Object> parsedRequest) {
        String fileSourceUri = servletRequestParser.getUri(parsedRequest);
        return contentFileProvider.getSourceFileAsStream(fileSourceUri);
    }
}
