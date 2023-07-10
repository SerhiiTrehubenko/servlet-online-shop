package com.tsa.shop.web.servlet;

import com.tsa.shop.cart.CartService;
import com.tsa.shop.domain.ProductDto;
import com.tsa.shop.domain.UriPageConnector;
import com.tsa.shop.logging.DomainLogger;
import com.tsa.shop.logmessagegenerator.LogMessageGenerator;
import com.tsa.shop.web.WebRequestHandler;
import com.tsa.shop.web.interfaces.PageGenerator;
import com.tsa.shop.web.interfaces.Response;
import com.tsa.shop.web.interfaces.ResponseWriter;
import com.tsa.shop.web.interfaces.ServletRequestParser;
import jakarta.servlet.http.Cookie;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class CartWebRequestHandler extends WebRequestHandler {

    private final CartService cartService;

    public CartWebRequestHandler(ServletRequestParser servletRequestParser,
                                 PageGenerator pageGenerator,
                                 ResponseWriter responseWriter,
                                 Response response,
                                 DomainLogger logger,
                                 LogMessageGenerator logMessageGenerator,
                                 CartService cartService) {
        super(servletRequestParser, pageGenerator, responseWriter, response, logger, logMessageGenerator);

        this.cartService = cartService;
    }

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
