package com.tsa.shop.servlets;

import com.tsa.shop.servlets.annot.Servlet;
import com.tsa.shop.servlets.enums.UriPageConnector;
import com.tsa.shop.servlets.interfaces.RequestHandler;
import com.tsa.shop.servlets.services.ProductRequestHandler;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Servlet
public class ProductServlet extends HttpServlet {

    private final static String URI = UriPageConnector.PRODUCTS.getUri() + "/*";
    private final RequestHandler requestHandler;

    public ProductServlet() {
        requestHandler = new ProductRequestHandler();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

        requestHandler.handleGet(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

        requestHandler.handlePost(request, response);
    }

    public String getUri() {
        return URI;
    }
}
