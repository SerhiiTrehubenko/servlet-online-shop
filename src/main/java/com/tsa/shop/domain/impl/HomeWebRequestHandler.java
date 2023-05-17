package com.tsa.shop.domain.impl;

import com.tsa.shop.domain.logging.DomainLogger;
import com.tsa.shop.domain.logmessagegenerator.LogMessageGenerator;
import com.tsa.shop.servlets.exceptions.WebServerException;
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
        Map<String, Object> parsedRequest = servletRequestParser.parseRequest(servletRequest);
        UriPageConnector pageConnector = servletRequestParser.getUriPageConnector(parsedRequest);

        try {
            InputStream content = InputStream.nullInputStream();
            if (homePage(pageConnector)) {
                content = getHomePage(pageConnector);
            } else if (file(pageConnector)) {
                content = getFileContent(parsedRequest);
            }
            writeSuccessResponse(servletResponse, content);
        } catch (WebServerException e) {
            String loggingMessage = logMessageGenerator.getMessageFrom(e);
            logError(parsedRequest.get(URL_FOR_ERROR_MESSAGE), loggingMessage);
            writeErrorResponse(servletResponse, e);

        } catch (RuntimeException e) {
            String loggingMessage = logMessageGenerator.getMessageFrom(e);
            logError(parsedRequest.get(URL_FOR_ERROR_MESSAGE), loggingMessage);
            writeDefaultErrorResponse(servletResponse);
        }
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
