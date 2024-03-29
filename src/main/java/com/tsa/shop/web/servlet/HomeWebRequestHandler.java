package com.tsa.shop.web.servlet;

import com.tsa.shop.logging.DomainLogger;
import com.tsa.shop.logmessagegenerator.LogMessageGenerator;
import com.tsa.shop.web.interfaces.*;
import com.tsa.shop.web.WebRequestHandler;
import com.tsa.shop.domain.UriPageConnector;

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
