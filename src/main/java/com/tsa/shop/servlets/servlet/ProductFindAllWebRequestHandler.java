package com.tsa.shop.servlets.servlet;

import com.tsa.shop.domain.interfaces.EntityService;
import com.tsa.shop.domain.interfaces.WebRequestHandler;
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

    public ProductFindAllWebRequestHandler(EntityService<T, E> service,
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
            InputStream content = getPageAllProducts(pageConnector.getHtmlPage());
            writeSuccessResponse(servletResponse, content);
        } catch (WebServerException e) {
            writeErrorResponse(servletResponse, e);
            logError(parsedRequest.get(URL_FOR_ERROR_MESSAGE), e);
        }
    }

    private InputStream getPageAllProducts(String htmlPageName) {
        List<E> productDtos = service.findAll();
        Map<String, Object> products = Map.of("products", productDtos);

        return pageGenerator.getGeneratedPageAsStream(products, htmlPageName);
    }
}
