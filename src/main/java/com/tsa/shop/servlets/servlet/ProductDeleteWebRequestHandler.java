package com.tsa.shop.servlets.servlet;

import com.tsa.shop.services.interfaces.ProductService;
import com.tsa.shop.servlets.WebRequestHandler;
import com.tsa.shop.logging.DomainLogger;
import com.tsa.shop.logmessagegenerator.LogMessageGenerator;
import com.tsa.shop.servlets.enums.UriPageConnector;
import com.tsa.shop.servlets.interfaces.PageGenerator;
import com.tsa.shop.servlets.interfaces.Response;
import com.tsa.shop.servlets.interfaces.ResponseWriter;
import com.tsa.shop.servlets.interfaces.ServletRequestParser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.InputStream;
import java.util.Map;

public class ProductDeleteWebRequestHandler extends WebRequestHandler {
    private final ProductService service;

    public ProductDeleteWebRequestHandler(ServletRequestParser servletRequestParser,
                                          PageGenerator pageGenerator,
                                          ResponseWriter responseWriter,
                                          Response response,
                                          DomainLogger logger,
                                          LogMessageGenerator logMessageGenerator,
                                          ProductService service) {
        super(servletRequestParser, pageGenerator, responseWriter, response, logger, logMessageGenerator);
        this.service = service;
    }

    @Override
    protected void doGet(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        getTemplateMethod(servletRequest, servletResponse);
        redirect(servletResponse, UriPageConnector.PRODUCTS.getUri());
    }

    @Override
    protected InputStream handleGetRequest(Map<String, Object> parsedRequest, UriPageConnector uriPageConnector) {
        Long productId = servletRequestParser.getIdFromRequest(parsedRequest);
        service.delete(productId);
        return InputStream.nullInputStream();
    }
}
