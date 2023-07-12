package com.tsa.shop.web.servlet;

import com.tsa.shop.application.AppContext;
import com.tsa.shop.cart.CartService;
import com.tsa.shop.domain.ProductDto;
import com.tsa.shop.domain.UriPageConnector;
import com.tsa.shop.web.WebRequestHandler;
import javax.servlet.http.Cookie;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class CartWebRequestHandler extends WebRequestHandler {

    private final CartService cartService = AppContext.get(CartService.class);

    protected InputStream handleGetRequest(Map<String, Object> parsedRequest, UriPageConnector uriPageConnector) {
        Cookie incomeToken = servletRequestParser.getTokenCookie(parsedRequest);
        List<ProductDto> productDtos = cartService.getAllProducts(incomeToken.getValue());
        Map<String, Object> products = Map.of("products", productDtos);

        return pageGenerator.getGeneratedPageAsStream(products, uriPageConnector.getHtmlPage());
    }

    @Override
    protected void handlePostRequest(Map<String, Object> parsedRequest) {
        Cookie incomeToken = servletRequestParser.getTokenCookie(parsedRequest);
        cartService.addToCart(servletRequestParser.getParameters(parsedRequest), incomeToken.getValue());
        redirect(getResponse(parsedRequest), UriPageConnector.PRODUCTS.getUri());
    }
}
