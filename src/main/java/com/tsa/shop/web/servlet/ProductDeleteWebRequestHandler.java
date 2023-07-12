package com.tsa.shop.web.servlet;

import com.tsa.shop.application.AppContext;
import com.tsa.shop.domain.ProductService;
import com.tsa.shop.web.WebRequestHandler;
import com.tsa.shop.domain.UriPageConnector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.InputStream;
import java.util.Map;

public class ProductDeleteWebRequestHandler extends WebRequestHandler {
    private final ProductService service = AppContext.get(ProductService.class);

    @Override
    protected void doGet(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        super.doGet(servletRequest, servletResponse);
        if (servletResponse.getStatus() == 200) {
            redirect(servletResponse, UriPageConnector.PRODUCTS.getUri());
        }
    }

    @Override
    protected InputStream handleGetRequest(Map<String, Object> parsedRequest, UriPageConnector uriPageConnector) {
        Long productId = servletRequestParser.getIdFromRequest(parsedRequest);
        service.delete(productId);
        return InputStream.nullInputStream();
    }
}
