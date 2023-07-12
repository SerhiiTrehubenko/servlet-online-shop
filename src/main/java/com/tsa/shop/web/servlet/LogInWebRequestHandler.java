package com.tsa.shop.web.servlet;

import com.tsa.shop.application.AppContext;
import com.tsa.shop.web.WebRequestHandler;
import com.tsa.shop.login.interfaces.LogInFacade;
import com.tsa.shop.domain.UriPageConnector;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import java.io.InputStream;
import java.util.Map;

public class LogInWebRequestHandler extends WebRequestHandler {

    private final LogInFacade logInFacade = AppContext.get(LogInFacade.class);

    @Override
    protected InputStream handleGetRequest(Map<String, Object> parsedRequest, UriPageConnector uriPageConnector) {
        return pageGenerator.getGeneratedPageAsStream(uriPageConnector.getHtmlPage());
    }

    @Override
    protected void handlePostRequest(Map<String, Object> parsedRequest) {
        Cookie cookieOnSuccess = authenticate(parsedRequest);
        writeToResponse(cookieOnSuccess, parsedRequest);
        redirect(getResponse(parsedRequest), UriPageConnector.HOME.getUri());
    }

    private Cookie authenticate(Map<String, Object> parsedRequest) {
        var parameters = servletRequestParser.getParameters(parsedRequest);
        return logInFacade.process(parameters);
    }

    private void writeToResponse(Cookie cookie, Map<String, Object> parsedRequest) {
        HttpServletResponse httpServletResponse = getResponse(parsedRequest);
        httpServletResponse.addCookie(cookie);
    }
}
