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

import java.util.Map;

public class ProductDeleteWebRequestHandler<T, E> extends WebRequestHandler {
    private final EntityService<T, E> service;

    public ProductDeleteWebRequestHandler(EntityService<T, E> service,
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
            deleteProduct(parsedRequest);
            redirect(servletResponse, UriPageConnector.PRODUCTS.getUri());
        } catch (WebServerException e) {
            writeErrorResponse(servletResponse, e);
            logError(parsedRequest.get(URL_FOR_ERROR_MESSAGE), e);
        }
    }

    private void deleteProduct(Map<String, Object> parsedRequest) {
        Long productId = servletRequestParser.getIdFromRequest(parsedRequest);
        service.delete(productId);
    }
}
