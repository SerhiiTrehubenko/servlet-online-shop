package com.tsa.shop.web.servlet;

import com.tsa.shop.application.AppContext;
import com.tsa.shop.domain.ProductDto;
import com.tsa.shop.domain.ProductService;
import com.tsa.shop.web.WebRequestHandler;
import com.tsa.shop.domain.UriPageConnector;

import java.io.InputStream;
import java.util.Map;

public class ProductUpdateWebRequestHandler extends WebRequestHandler {
    private final ProductService service = AppContext.get(ProductService.class);

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
