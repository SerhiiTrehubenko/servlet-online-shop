package com.tsa.shop.servlets;

import com.tsa.shop.servlets.annot.Servlet;
import com.tsa.shop.servlets.interfaces.RequestHandler;
import com.tsa.shop.servlets.services.DefaultRequestHandler;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Servlet
public class ContentFilesServlet extends HttpServlet {

    private final static String URI = "/";
    private final RequestHandler requestHandler;

    public ContentFilesServlet() {
        requestHandler = new DefaultRequestHandler();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

        requestHandler.handleGet(request, response);

    }

    public String getUri() {
        return URI;
    }
}
