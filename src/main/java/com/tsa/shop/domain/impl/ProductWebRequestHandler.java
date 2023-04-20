package com.tsa.shop.domain.impl;

import com.tsa.shop.domain.interfaces.EntityService;
import com.tsa.shop.servlets.enums.HttpStatus;
import com.tsa.shop.servlets.enums.UriPageConnector;
import com.tsa.shop.servlets.exceptions.WebServerException;
import com.tsa.shop.domain.interfaces.WebRequestHandler;
import com.tsa.shop.servlets.interfaces.PageGenerator;
import com.tsa.shop.servlets.interfaces.Response;
import com.tsa.shop.servlets.interfaces.ServletRequestParser;
import com.tsa.shop.servlets.interfaces.ResponseWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class ProductWebRequestHandler<T, E> extends WebRequestHandler {

    private final static String PAGE_NOT_FOUND_MESSAGE = "the Page is not found";
    private final EntityService<T, E> service;

    public ProductWebRequestHandler(EntityService<T, E> service,
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
            InputStream content = InputStream.nullInputStream();
            if (pageAllProducts(pageConnector)) {
                content = getPageAllProducts(pageConnector.getHtmlPage());

            } else if (pageDeleteProduct(pageConnector)) {
                deleteProduct(parsedRequest);
                redirect(servletResponse, UriPageConnector.PRODUCTS.getUri());

            } else if (pageUpdateProduct(pageConnector)) {
                content = getPageUpdateProduct(parsedRequest, pageConnector.getHtmlPage());

            } else if (pageAddProduct(pageConnector)) {
                content = getPageAddProduct(pageConnector.getHtmlPage());

            } else {
                throw new WebServerException(PAGE_NOT_FOUND_MESSAGE, HttpStatus.NOT_FOUND);
            }

            writeSuccessResponse(servletResponse, content);

        } catch (WebServerException e) {
            writeErrorResponse(servletResponse, e);
            logError(parsedRequest.get(URL_FOR_ERROR_MESSAGE), e);
        }
    }

    private boolean pageAllProducts(UriPageConnector pageConnector) {
        return UriPageConnector.PRODUCTS == pageConnector;
    }

    private InputStream getPageAllProducts(String htmlPageName) {
        List<E> productDtos = service.findAll();
        Map<String, Object> products = Map.of("products", productDtos);

        return pageGenerator.getGeneratedPageAsStream(products, htmlPageName);
    }

    private boolean pageDeleteProduct(UriPageConnector pageConnector) {
        return UriPageConnector.PRODUCTS_DELETE == pageConnector;
    }

    private void deleteProduct(Map<String, Object> parsedRequest) {
        Long productId = servletRequestParser.getIdFromRequest(parsedRequest);
        service.delete(productId);
    }

    private boolean pageUpdateProduct(UriPageConnector pageConnector) {
        return UriPageConnector.PRODUCTS_UPDATE == pageConnector;
    }

    private InputStream getPageUpdateProduct(Map<String, Object> parsedRequest, String htmlPageName) {
        Long productId = servletRequestParser.getIdFromRequest(parsedRequest);
        E productDto = service.findById(productId);

        parsedRequest.put("productDto", productDto);

        return pageGenerator.getGeneratedPageAsStream(parsedRequest, htmlPageName);
    }

    private boolean pageAddProduct(UriPageConnector pageConnector) {
        return UriPageConnector.PRODUCTS_ADD == pageConnector;
    }

    private InputStream getPageAddProduct(String htmlPageName) {
        Map<String, Object> emptyContent = Map.of();
        return pageGenerator.getGeneratedPageAsStream(emptyContent, htmlPageName);
    }

    @Override
    protected void doPost(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        Map<String, Object> parsedRequest = servletRequestParser.parseRequest(servletRequest);

        try {
            UriPageConnector pageConnector = servletRequestParser.getUriPageConnector(parsedRequest);
            var parameters = servletRequestParser.getParameters(parsedRequest);
            if (postUpdate(pageConnector)) {
                service.update(parameters);

            } else if (pageAddProduct(pageConnector)) {
                service.add(parameters);

            } else {
                throw new WebServerException(PAGE_NOT_FOUND_MESSAGE, HttpStatus.NOT_FOUND);
            }
            redirect(servletResponse, UriPageConnector.PRODUCTS.getUri());

        } catch (WebServerException e) {
            writeErrorResponse(servletResponse, e);
            logError(parsedRequest.get(URL_FOR_ERROR_MESSAGE), e);
        }
    }

    private boolean postUpdate(UriPageConnector pageConnector) {
        return UriPageConnector.PRODUCTS_POST_UPDATE == pageConnector;
    }

    private void redirect(HttpServletResponse response, String uri) throws WebServerException {
        try {
            response.sendRedirect(uri);
        } catch (IOException e) {
            throw new WebServerException(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
