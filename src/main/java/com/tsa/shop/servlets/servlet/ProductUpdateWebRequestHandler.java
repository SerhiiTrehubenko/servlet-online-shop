package com.tsa.shop.servlets.servlet;

import com.tsa.shop.domain.interfaces.EntityService;
import com.tsa.shop.domain.interfaces.WebRequestHandler;
import com.tsa.shop.domain.logging.DomainLogger;
import com.tsa.shop.domain.logmessagegenerator.LogMessageGenerator;
import com.tsa.shop.servlets.enums.UriPageConnector;
import com.tsa.shop.servlets.exceptions.WebServerException;
import com.tsa.shop.servlets.interfaces.PageGenerator;
import com.tsa.shop.servlets.interfaces.Response;
import com.tsa.shop.servlets.interfaces.ResponseWriter;
import com.tsa.shop.servlets.interfaces.ServletRequestParser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.InputStream;
import java.util.Map;

public class ProductUpdateWebRequestHandler<T, E> extends WebRequestHandler {
    private final EntityService<T, E> service;

    public ProductUpdateWebRequestHandler(ServletRequestParser servletRequestParser,
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
            InputStream content = getPageUpdateProduct(parsedRequest, pageConnector.getHtmlPage());
            writeSuccessResponse(servletResponse, content);
        } catch (WebServerException e) {
            String loggingMessage = logMessageGenerator.getMessageFrom(e);
            logError(parsedRequest.get(URL_FOR_ERROR_MESSAGE), loggingMessage);
            writeErrorResponse(servletResponse, e);
        } catch (RuntimeException e) {
            String loggingMessage = logMessageGenerator.getMessageFrom(e);
            logError(parsedRequest.get(URL_FOR_ERROR_MESSAGE), loggingMessage);
        }
    }

    private InputStream getPageUpdateProduct(Map<String, Object> parsedRequest, String htmlPageName) {
        Long productId = servletRequestParser.getIdFromRequest(parsedRequest);
        E productDto = service.findById(productId);

        parsedRequest.put("productDto", productDto);

        return pageGenerator.getGeneratedPageAsStream(parsedRequest, htmlPageName);
    }

    @Override
    protected void doPost(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        Map<String, Object> parsedRequest = servletRequestParser.parseRequest(servletRequest);
        try {
            var parameters = servletRequestParser.getParameters(parsedRequest);
            service.update(parameters);
            redirect(servletResponse, UriPageConnector.PRODUCTS.getUri());
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
}
