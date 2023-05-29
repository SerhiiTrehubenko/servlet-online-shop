package com.tsa.shop.web.servlet;

import com.tsa.shop.domain.ProductDto;
import com.tsa.shop.domain.ProductService;
import com.tsa.shop.web.interfaces.PageGenerator;
import com.tsa.shop.web.interfaces.Response;
import com.tsa.shop.web.interfaces.ResponseWriter;
import com.tsa.shop.web.interfaces.ServletRequestParser;
import com.tsa.shop.web.WebRequestHandler;
import com.tsa.shop.logging.DomainLogger;
import com.tsa.shop.logmessagegenerator.LogMessageGenerator;
import com.tsa.shop.domain.UriPageConnector;

import java.io.InputStream;
import java.util.Map;

public class ProductUpdateWebRequestHandler extends WebRequestHandler {
    private final ProductService service;

    public ProductUpdateWebRequestHandler(ServletRequestParser servletRequestParser,
                                          PageGenerator pageGenerator,
                                          ResponseWriter responseWriter,
                                          Response response,
                                          DomainLogger logger,
                                          LogMessageGenerator logMessageGenerator,
                                          ProductService service) {
        super(servletRequestParser, pageGenerator, responseWriter, response, logger, logMessageGenerator);
        this.service = service;
    }

    protected InputStream handleGetRequest(Map<String, Object> parsedRequest, UriPageConnector uriPageConnector) {
        Long productId = servletRequestParser.getIdFromRequest(parsedRequest);
        ProductDto productDto = service.findById(productId);

        parsedRequest.put("productDto", productDto);

        return pageGenerator.getGeneratedPageAsStream(parsedRequest, uriPageConnector.getHtmlPage());
    }

    @Override
    protected void handlePostRequest(Map<String, Object> parsedRequest) {
        var parameters = servletRequestParser.getParameters(parsedRequest);
        service.update(parameters);
        redirect(getResponse(parsedRequest), UriPageConnector.PRODUCTS.getUri());
    }
}
