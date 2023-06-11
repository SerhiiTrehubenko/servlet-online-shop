package com.tsa.shop.web;

import com.tsa.shop.logging.DomainLogger;
import com.tsa.shop.logmessagegenerator.LogMessageGenerator;
import com.tsa.shop.domain.HttpStatus;
import com.tsa.shop.domain.UriPageConnector;
import com.tsa.shop.domain.WebServerException;
import com.tsa.shop.web.interfaces.PageGenerator;
import com.tsa.shop.web.interfaces.Response;
import com.tsa.shop.web.interfaces.ResponseWriter;
import com.tsa.shop.web.interfaces.ServletRequestParser;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;

public abstract class WebRequestHandler extends HttpServlet {
    protected final static String URL_FOR_ERROR_MESSAGE = "URL";
    private final static String KEY_REQUEST = "HttpServletRequest";
    private final static String KEY_RESPONSE = "HttpServletResponse";
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
        this.logger = logger.getLogger(this.getClass());
        this.logMessageGenerator = logMessageGenerator;
    }

    @Override
    protected void doGet(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        Map<String, Object> webDtoHost = makeWebDtoHost(servletRequest, servletResponse);
        try {
            UriPageConnector pageConnector = servletRequestParser.getUriPageConnector(webDtoHost);
            InputStream content = handleGetRequest(webDtoHost, pageConnector);
            writeSuccessResponse(servletResponse, content);
        } catch (WebServerException e) {
            processWebServerException(webDtoHost, e);
        } catch (RuntimeException e) {
            processInternalException(webDtoHost, e);
        }
    }

    private Map<String, Object> makeWebDtoHost(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        Map<String, Object> parsedRequest = servletRequestParser.parseRequest(servletRequest);
        parsedRequest.put(KEY_REQUEST, servletRequest);
        parsedRequest.put(KEY_RESPONSE, servletResponse);
        return parsedRequest;
    }

    protected InputStream handleGetRequest(Map<String, Object> parsedRequest, UriPageConnector uriPageConnector) {
        throw new UnsupportedOperationException();
    }
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

    private void processWebServerException(Map<String, Object> webDtoHost, WebServerException e) {
        String loggingMessage = logMessageGenerator.getMessageFrom(e);
        logError(webDtoHost.get(URL_FOR_ERROR_MESSAGE), loggingMessage);
        writeErrorResponse(getResponse(webDtoHost), e);
    }

    protected HttpServletResponse getResponse(Map<String, Object> parsedRequest) {
        return (HttpServletResponse) parsedRequest.get(KEY_RESPONSE);
    }

    protected void logError(Object requestUri, String message) {
        String header = "HttpRequest URL: " + requestUri + " Time of request: " + new Date();
        logger.error(header + "\n" + message);
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

    private void processInternalException(Map<String, Object> webDtoHost, RuntimeException e) {
        String loggingMessage = logMessageGenerator.getMessageFrom(e);
        logError(webDtoHost.get(URL_FOR_ERROR_MESSAGE), loggingMessage);
        writeDefaultErrorResponse(getResponse(webDtoHost));
    }

    protected void writeDefaultErrorResponse(HttpServletResponse servletResponse) {
        var error = new WebServerException("Oooops! Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        writeErrorResponse(servletResponse, error);
    }

    @Override
    protected void doPost(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        Map<String, Object> webDtoHost = makeWebDtoHost(servletRequest, servletResponse);
        try {
            handlePostRequest(webDtoHost);
        } catch (WebServerException e) {
            processWebServerException(webDtoHost, e);
        } catch (RuntimeException e) {
            processInternalException(webDtoHost, e);
        }
    }

    protected void handlePostRequest(Map<String, Object> parsedRequest) {
        throw new UnsupportedOperationException();
    }

    protected HttpServletRequest getRequest(Map<String, Object> parsedRequest) {
        return (HttpServletRequest) parsedRequest.get(KEY_REQUEST);
    }

    protected void redirect(HttpServletResponse response, String uri) {
        try {
            response.sendRedirect(uri);
        } catch (IOException e) {
            throw new WebServerException(e, HttpStatus.INTERNAL_SERVER_ERROR, this);
        }
    }

    public void publicPost(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        try {
            doPost(servletRequest, servletResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void publicGet(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        doGet(servletRequest, servletResponse);
    }
}
