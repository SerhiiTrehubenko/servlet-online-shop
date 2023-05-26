package com.tsa.shop.servlets.servlet;

import com.tsa.shop.domain.interfaces.WebRequestHandler;
import com.tsa.shop.domain.logging.DomainLogger;
import com.tsa.shop.domain.login.interfaces.LogInFacade;
import com.tsa.shop.domain.logmessagegenerator.LogMessageGenerator;
import com.tsa.shop.servlets.enums.UriPageConnector;
import com.tsa.shop.servlets.interfaces.PageGenerator;
import com.tsa.shop.servlets.interfaces.Response;
import com.tsa.shop.servlets.interfaces.ResponseWriter;
import com.tsa.shop.servlets.interfaces.ServletRequestParser;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.InputStream;
import java.util.Map;

public class LogInWebRequestHandler extends WebRequestHandler {

    private final LogInFacade logInFacade;
    public LogInWebRequestHandler(ServletRequestParser servletRequestParser,
                                  PageGenerator pageGenerator,
                                  ResponseWriter responseWriter,
                                  Response response,
                                  DomainLogger logger,
                                  LogMessageGenerator logMessageGenerator,
                                  LogInFacade logInFacade) {
        super(servletRequestParser, pageGenerator, responseWriter, response, logger, logMessageGenerator);
        this.logInFacade = logInFacade;
    }

    @Override
    protected void doGet(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        getTemplateMethod(servletRequest, servletResponse);
    }

    @Override
    protected InputStream handleGetRequest(Map<String, Object> parsedRequest, UriPageConnector uriPageConnector) {
        return pageGenerator.getGeneratedPageAsStream(uriPageConnector.getHtmlPage());
    }

    @Override
    protected void doPost(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        postTemplateMethod(servletRequest, servletResponse);
    }

    @Override
    protected void handlePostRequest(Map<String, Object> parsedRequest) {
        var parameters = servletRequestParser.getParameters(parsedRequest);
        final Cookie cookie = logInFacade.process(parameters);
        HttpServletResponse httpServletResponse = getResponse(parsedRequest);
        httpServletResponse.addCookie(cookie);
        redirect(httpServletResponse, UriPageConnector.HOME.getUri());
    }
}
