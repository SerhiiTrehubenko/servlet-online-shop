package com.tsa.shop.web.servlet;

import com.tsa.shop.application.AppContext;
import com.tsa.shop.domain.ProductDto;
import com.tsa.shop.domain.ProductService;
import com.tsa.shop.web.WebRequestHandler;
import com.tsa.shop.domain.UriPageConnector;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class ProductFindAllWebRequestHandler extends WebRequestHandler {

    private final ProductService service = AppContext.get(ProductService.class);

    protected InputStream handleGetRequest(Map<String, Object> parsedRequest, UriPageConnector uriPageConnector) {
        List<ProductDto> productDtos = service.findAll();
        Map<String, Object> products = Map.of("products", productDtos);

        return pageGenerator.getGeneratedPageAsStream(products, uriPageConnector.getHtmlPage());
    }
}
