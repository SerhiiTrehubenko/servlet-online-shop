package com.tsa.shop.web.servlet;

import com.tsa.shop.domain.ProductDto;
import com.tsa.shop.domain.ProductService;
import com.tsa.shop.domain.UriPageConnector;
import com.tsa.shop.logging.DomainLogger;
import com.tsa.shop.logmessagegenerator.LogMessageGenerator;
import com.tsa.shop.web.WebRequestHandler;
import com.tsa.shop.web.interfaces.PageGenerator;
import com.tsa.shop.web.interfaces.Response;
import com.tsa.shop.web.interfaces.ResponseWriter;
import com.tsa.shop.web.interfaces.ServletRequestParser;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProductFilterWebRequestHandler extends WebRequestHandler {
    private final ProductService productService;

    public ProductFilterWebRequestHandler(ServletRequestParser servletRequestParser,
                                          PageGenerator pageGenerator,
                                          ResponseWriter responseWriter,
                                          Response response,
                                          DomainLogger domainLogger,
                                          LogMessageGenerator logMessageGenerator,
                                          ProductService productService) {
        super(servletRequestParser, pageGenerator, responseWriter, response, domainLogger, logMessageGenerator);
        this.productService = productService;
    }

    @Override
    protected void handlePostRequest(Map<String, Object> parsedRequest) {
        Map<String, String[]> parameters = servletRequestParser.getParameters(parsedRequest);
        UriPageConnector uriPageConnector = servletRequestParser.getUriPageConnector(parsedRequest);
        String criteria = Objects.toString(parameters.get("criteria")[0]);

        List<ProductDto> filteredProductDtos = productService.findByCriteria(criteria);
        Map<String, Object> products = Map.of("products", filteredProductDtos);
        InputStream generatedPageAsStream = pageGenerator.getGeneratedPageAsStream(products, uriPageConnector.getHtmlPage());
        writeSuccessResponse(getResponse(parsedRequest), generatedPageAsStream);
    }
}
