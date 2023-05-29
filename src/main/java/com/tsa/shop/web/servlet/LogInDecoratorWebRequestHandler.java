package com.tsa.shop.web.servlet;

import com.tsa.shop.web.interfaces.PageGenerator;
import com.tsa.shop.web.interfaces.Response;
import com.tsa.shop.web.interfaces.ResponseWriter;
import com.tsa.shop.web.interfaces.ServletRequestParser;
import com.tsa.shop.web.WebRequestHandler;
import com.tsa.shop.logging.DomainLogger;
import com.tsa.shop.login.repo.TokenRepository;
import com.tsa.shop.logmessagegenerator.LogMessageGenerator;
import com.tsa.shop.domain.UriPageConnector;
import jakarta.servlet.http.Cookie;

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
    protected InputStream handleGetRequest(Map<String, Object> parsedRequest, UriPageConnector uriPageConnector) {
        if (tokenPresent(parsedRequest)) {
            webRequestHandler.publicGet(getRequest(parsedRequest), getResponse(parsedRequest));
        } else {
            redirectToHomePage(parsedRequest);
        }
        return InputStream.nullInputStream();
    }
    private boolean tokenPresent(Map<String, Object> parsedRequest) {
        Cookie cookie = getTokenCookie(parsedRequest);
        return tokenRepository.isPresent(cookie.getValue());
    }
    private Cookie getTokenCookie(Map<String, Object> parsedRequest) {
        return servletRequestParser.getTokenCookie(parsedRequest);
    }

    private void redirectToHomePage(Map<String, Object> parsedRequest) {
        redirect(getResponse(parsedRequest), UriPageConnector.LOG_IN_PAGE.getUri());
    }

    @Override
    protected void handlePostRequest(Map<String, Object> parsedRequest) {
        if (tokenPresent(parsedRequest)) {
            webRequestHandler.publicPost(getRequest(parsedRequest), getResponse(parsedRequest));
        } else {
            redirectToHomePage(parsedRequest);
        }
    }
}
