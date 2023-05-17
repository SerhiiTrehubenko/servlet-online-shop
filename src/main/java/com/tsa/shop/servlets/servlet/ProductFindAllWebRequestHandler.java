package com.tsa.shop.servlets.servlet;

import com.tsa.shop.domain.interfaces.EntityService;
import com.tsa.shop.domain.interfaces.WebRequestHandler;
import com.tsa.shop.domain.logging.DomainLogger;
import com.tsa.shop.domain.logmessagegenerator.LogMessageGenerator;
import com.tsa.shop.servlets.enums.UriPageConnector;
import com.tsa.shop.servlets.exceptions.WebServerException;
import com.tsa.shop.servlets.interfaces.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class ProductFindAllWebRequestHandler<T, E> extends WebRequestHandler {

    private final EntityService<T, E> service;

    public ProductFindAllWebRequestHandler(ServletRequestParser servletRequestParser,
                                           PageGenerator pageGenerator,
                                           ResponseWriter responseWriter,
                                           Response response,
                                           DomainLogger logger,
                                           LogMessageGenerator logMessageGenerator,
                                           EntityService<T, E> service) {
        super(servletRequestParser, pageGenerator, responseWriter, response, logger, logMessageGenerator);
        this.service = service;
    }

    @Override
    protected void doGet(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        Map<String, Object> parsedRequest = servletRequestParser.parseRequest(servletRequest);
        try {
            UriPageConnector pageConnector = servletRequestParser.getUriPageConnector(parsedRequest);
            InputStream content = getPageAllProducts(pageConnector.getHtmlPage());
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

    private InputStream getPageAllProducts(String htmlPageName) {
        List<E> productDtos = service.findAll();
        Map<String, Object> products = Map.of("products", productDtos);

        return pageGenerator.getGeneratedPageAsStream(products, htmlPageName);
    }
}
