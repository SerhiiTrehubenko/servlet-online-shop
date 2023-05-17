package com.tsa.shop.servlets.servlet;

import com.tsa.shop.domain.interfaces.WebRequestHandler;
import com.tsa.shop.domain.logging.DomainLogger;
import com.tsa.shop.domain.logmessagegenerator.LogMessageGenerator;
import com.tsa.shop.servlets.enums.HttpStatus;
import com.tsa.shop.servlets.exceptions.WebServerException;
import com.tsa.shop.servlets.interfaces.PageGenerator;
import com.tsa.shop.servlets.interfaces.Response;
import com.tsa.shop.servlets.interfaces.ResponseWriter;
import com.tsa.shop.servlets.interfaces.ServletRequestParser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
    protected void doGet(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        writePageNotFoundResponse(servletRequest, servletResponse);
    }

    private void writePageNotFoundResponse(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        Map<String, Object> parsedRequest = servletRequestParser.parseRequest(servletRequest);
        try {
            throw new WebServerException(PAGE_NOT_FOUND_MESSAGE.formatted(parsedRequest.get(URL_FOR_ERROR_MESSAGE)), HttpStatus.NOT_FOUND);
        } catch (WebServerException e) {
            String loggingMessage = logMessageGenerator.getMessageFrom(e);
            logError(parsedRequest.get(URL_FOR_ERROR_MESSAGE), loggingMessage);
            writeErrorResponse(servletResponse, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        writePageNotFoundResponse(servletRequest, servletResponse);
    }
}
