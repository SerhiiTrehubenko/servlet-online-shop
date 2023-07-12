package com.tsa.shop.web.servlet;

import com.tsa.shop.application.AppContext;
import com.tsa.shop.domain.UriPageConnector;
import com.tsa.shop.logout.LogoutService;
import com.tsa.shop.web.WebRequestHandler;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.InputStream;
import java.util.Map;

public class LogoutWebRequestHandler extends WebRequestHandler {

    private final LogoutService logoutService = AppContext.get(LogoutService.class);

    @Override
    protected void doGet(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        super.doGet(servletRequest, servletResponse);
        if (servletResponse.getStatus() == 200) {
            redirect(servletResponse, UriPageConnector.HOME.getUri());
        }
    }

    @Override
    protected InputStream handleGetRequest(Map<String, Object> parsedRequest, UriPageConnector uriPageConnector) {
        Cookie tokenCookie = servletRequestParser.getTokenCookie(parsedRequest);
        logoutService.logout(tokenCookie);
        return InputStream.nullInputStream();
    }
}
