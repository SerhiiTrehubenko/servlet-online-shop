package com.tsa.shop.servlets.servlet;

import com.tsa.shop.servlets.WebRequestHandler;
import com.tsa.shop.logging.DomainLogger;
import com.tsa.shop.login.repo.TokenRepository;
import com.tsa.shop.logmessagegenerator.LogMessageGenerator;
import com.tsa.shop.domain.UriPageConnector;
import com.tsa.shop.servlets.interfaces.PageGenerator;
import com.tsa.shop.servlets.interfaces.Response;
import com.tsa.shop.servlets.interfaces.ResponseWriter;
import com.tsa.shop.servlets.interfaces.ServletRequestParser;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.InputStream;
import java.util.Map;

public class LogInDecoratorWebRequestHandler extends WebRequestHandler {

    private final TokenRepository tokenRepository;
    private final WebRequestHandler webRequestHandler;

    public LogInDecoratorWebRequestHandler(ServletRequestParser servletRequestParser,
                                           PageGenerator pageGenerator,
                                           ResponseWriter responseWriter,
                                           Response response,
                                           DomainLogger logger,
                                           LogMessageGenerator logMessageGenerator,
                                           TokenRepository tokenRepository,
                                           WebRequestHandler webRequestHandler) {
        super(servletRequestParser, pageGenerator, responseWriter, response, logger, logMessageGenerator);
        this.tokenRepository = tokenRepository;
        this.webRequestHandler = webRequestHandler;
    }

    @Override
    protected void doGet(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        getTemplateMethod(servletRequest, servletResponse);
    }

    @Override
    protected InputStream handleGetRequest(Map<String, Object> parsedRequest, UriPageConnector uriPageConnector) {
        Cookie cookie = getTokenCookie(parsedRequest);
        if (tokenRepository.isPresent(cookie.getValue())) {
            webRequestHandler.publicGet(getRequest(parsedRequest), getResponse(parsedRequest));
        } else {
            redirectToHomePage(parsedRequest);
        }
        return InputStream.nullInputStream();
    }

    private Cookie getTokenCookie(Map<String, Object> parsedRequest) {
        return servletRequestParser.getTokenCookie(parsedRequest);
    }

    private void redirectToHomePage(Map<String, Object> parsedRequest) {
        redirect(getResponse(parsedRequest), UriPageConnector.LOG_IN_PAGE.getUri());
    }

    @Override
    protected void doPost(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        postTemplateMethod(servletRequest, servletResponse);
    }

    @Override
    protected void handlePostRequest(Map<String, Object> parsedRequest) {
        Cookie cookie = getTokenCookie(parsedRequest);
        if (tokenRepository.isPresent(cookie.getValue())) {
            webRequestHandler.publicPost(getRequest(parsedRequest), getResponse(parsedRequest));
        } else {
            redirectToHomePage(parsedRequest);
        }
    }


}
