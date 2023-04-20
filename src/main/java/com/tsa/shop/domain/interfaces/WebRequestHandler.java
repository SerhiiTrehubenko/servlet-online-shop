package com.tsa.shop.domain.interfaces;

import com.tsa.shop.servlets.enums.HttpStatus;
import com.tsa.shop.servlets.enums.UriPageConnector;
import com.tsa.shop.servlets.exceptions.WebServerException;
import com.tsa.shop.servlets.interfaces.PageGenerator;
import com.tsa.shop.servlets.interfaces.ServletRequestParser;
import com.tsa.shop.servlets.interfaces.Response;
import com.tsa.shop.servlets.interfaces.ResponseWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;

public abstract class WebRequestHandler extends HttpServlet {
    protected final static String URL_FOR_ERROR_MESSAGE = "URL";
    protected final ServletRequestParser servletRequestParser;
    protected final PageGenerator pageGenerator;
    protected final ResponseWriter responseWriter;
    protected final Response response;

    public WebRequestHandler(ServletRequestParser servletRequestParser,
                             PageGenerator pageGenerator,
                             ResponseWriter responseWriter,
                             Response response) {
        this.servletRequestParser = servletRequestParser;
        this.pageGenerator = pageGenerator;
        this.responseWriter = responseWriter;
        this.response = response;
    }

    @Override
    protected abstract void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;

    protected void writeSuccessResponse(HttpServletResponse servletResponse, InputStream content) {
        Response successResponse = getSuccessResponse(servletResponse, content);
        responseWriter.createInstance().write(successResponse);
    }

    protected Response getSuccessResponse(HttpServletResponse servletResponse, InputStream content) {
        return response.createResponse()
                .setServletResponse(servletResponse)
                .setHttpStatus(HttpStatus.OK.getCode())
                .setContent(content);
    }

    protected void writeErrorResponse(HttpServletResponse servletResponse, WebServerException e) {
        Response errorResponse = getErrorResponse(servletResponse, e);
        responseWriter.createInstance().write(errorResponse);
    }

    private Response getErrorResponse(HttpServletResponse servletResponse, WebServerException e) {
        InputStream errorPage = getErrorPage(e);
        return response.createResponse()
                .setServletResponse(servletResponse)
                .setHttpStatus(e.getHttpStatus().getCode())
                .setContent(errorPage);
    }

    private InputStream getErrorPage(WebServerException e) {
        Map<String, Object> message = Map.of("errorMessage", e.getMessage());
        return pageGenerator.getGeneratedPageAsStream(message, UriPageConnector.ERROR_PAGE.getHtmlPage());
    }

    protected void logError(Object requestUri, WebServerException e) {
        System.out.println("HttpRequest URL: " + requestUri + " Time of request: " + new Date());
        e.printStackTrace();
    }
}
