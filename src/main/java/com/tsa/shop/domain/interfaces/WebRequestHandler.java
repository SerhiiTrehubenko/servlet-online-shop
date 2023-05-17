package com.tsa.shop.domain.interfaces;

import com.tsa.shop.domain.logging.DomainLogger;
import com.tsa.shop.domain.logmessagegenerator.LogMessageGenerator;
import com.tsa.shop.servlets.enums.HttpStatus;
import com.tsa.shop.servlets.enums.UriPageConnector;
import com.tsa.shop.servlets.exceptions.WebServerException;
import com.tsa.shop.servlets.interfaces.PageGenerator;
import com.tsa.shop.servlets.interfaces.ServletRequestParser;
import com.tsa.shop.servlets.interfaces.Response;
import com.tsa.shop.servlets.interfaces.ResponseWriter;
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
    protected final DomainLogger logger;
    protected final LogMessageGenerator logMessageGenerator;


    public WebRequestHandler(ServletRequestParser servletRequestParser,
                             PageGenerator pageGenerator,
                             ResponseWriter responseWriter,
                             Response response,
                             DomainLogger logger,
                             LogMessageGenerator logMessageGenerator) {
        this.servletRequestParser = servletRequestParser;
        this.pageGenerator = pageGenerator;
        this.responseWriter = responseWriter;
        this.response = response;
        this.logger = logger;
        logger.setClass(this.getClass());
        this.logMessageGenerator = logMessageGenerator;
    }

    @Override
    protected abstract void doGet(HttpServletRequest req, HttpServletResponse resp);

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

    protected void writeDefaultErrorResponse(HttpServletResponse servletResponse) {
        var error = new WebServerException("Oooops! Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        writeErrorResponse(servletResponse, error);
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

    protected void logError(Object requestUri, String message) {
        String header = "HttpRequest URL: " + requestUri + " Time of request: " + new Date();
        logger.error(header + "\n" + message);
    }

    protected void redirect(HttpServletResponse response, String uri) {
        try {
            response.sendRedirect(uri);
        } catch (IOException e) {
            throw new WebServerException(e, HttpStatus.INTERNAL_SERVER_ERROR, this);
        }
    }
}
