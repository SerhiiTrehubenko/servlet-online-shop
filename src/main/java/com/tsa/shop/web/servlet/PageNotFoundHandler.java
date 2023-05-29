package com.tsa.shop.web.servlet;

import com.tsa.shop.domain.UriPageConnector;
import com.tsa.shop.web.WebRequestHandler;
import com.tsa.shop.web.interfaces.PageGenerator;
import com.tsa.shop.web.interfaces.Response;
import com.tsa.shop.web.interfaces.ResponseWriter;
import com.tsa.shop.web.interfaces.ServletRequestParser;
import com.tsa.shop.logging.DomainLogger;
import com.tsa.shop.logmessagegenerator.LogMessageGenerator;
import com.tsa.shop.domain.HttpStatus;
import com.tsa.shop.domain.WebServerException;

import java.io.InputStream;
import java.util.Map;

public class PageNotFoundHandler extends WebRequestHandler {

    private final static String PAGE_NOT_FOUND_MESSAGE = "the Page with URL: [%s] is not found";

    public PageNotFoundHandler(ServletRequestParser servletRequestParser,
                               PageGenerator pageGenerator,
                               ResponseWriter responseWriter,
                               Response response,
                               DomainLogger logger,
                               LogMessageGenerator logMessageGenerator) {
        super(servletRequestParser, pageGenerator, responseWriter, response, logger, logMessageGenerator);
    }

    @Override
    protected InputStream handleGetRequest(Map<String, Object> parsedRequest, UriPageConnector uriPageConnector) {
        throw defaultException(parsedRequest);
    }

    private WebServerException defaultException(Map<String, Object> parsedRequest) {
        return new WebServerException(PAGE_NOT_FOUND_MESSAGE.formatted(parsedRequest.get(URL_FOR_ERROR_MESSAGE)), HttpStatus.NOT_FOUND, this);
    }

    @Override
    protected void handlePostRequest(Map<String, Object> parsedRequest) {
        throw defaultException(parsedRequest);
    }
}
