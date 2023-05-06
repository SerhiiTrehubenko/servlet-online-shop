package com.tsa.shop.servlets.servlet;

import com.tsa.shop.domain.interfaces.EntityService;
import com.tsa.shop.domain.interfaces.WebRequestHandler;
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

    public ProductUpdateWebRequestHandler(EntityService<T, E> service,
                                           ServletRequestParser servletRequestParser,
                                           PageGenerator pageGenerator,
                                           Response response,
                                           ResponseWriter responseWriter) {
        super(servletRequestParser, pageGenerator, responseWriter, response);
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
            writeErrorResponse(servletResponse, e);
            logError(parsedRequest.get(URL_FOR_ERROR_MESSAGE), e);
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
            writeErrorResponse(servletResponse, e);
            logError(parsedRequest.get(URL_FOR_ERROR_MESSAGE), e);
        }
    }
}
