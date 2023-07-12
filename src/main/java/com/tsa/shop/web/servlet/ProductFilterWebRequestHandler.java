package com.tsa.shop.web.servlet;

import com.tsa.shop.application.AppContext;
import com.tsa.shop.domain.ProductDto;
import com.tsa.shop.domain.ProductService;
import com.tsa.shop.domain.UriPageConnector;
import com.tsa.shop.web.WebRequestHandler;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProductFilterWebRequestHandler extends WebRequestHandler {
    private final ProductService productService = AppContext.get(ProductService.class);

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
