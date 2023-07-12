package com.tsa.shop.web.impl;

import com.tsa.shop.application.AppContext;
import com.tsa.shop.domain.UriPageConnector;
import com.tsa.shop.login.repo.TokenRepository;
import com.tsa.shop.web.interfaces.ServletRequestParser;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class LogInFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }

    private final TokenRepository tokenRepository = AppContext.get(TokenRepository.class);
    private final ServletRequestParser servletRequestParser = AppContext.get(ServletRequestParser.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        Map<String, Object> parsedRequest = servletRequestParser.parseRequest((HttpServletRequest) request);
        Host host = new Host(parsedRequest, request, response, chain);
        UriPageConnector pageConnector = servletRequestParser.getUriPageConnector(parsedRequest);

        if (requestForManipulationOnSite(pageConnector)) {
            addressRequest(host);
        } else {
            redirectToHtmlSourceElement(host);
        }
    }

    private boolean requestForManipulationOnSite(UriPageConnector pageConnector) {
        return Objects.nonNull(pageConnector) &&
                (pageConnector != UriPageConnector.LOG_IN_PAGE) &&
                (pageConnector != UriPageConnector.LOG_OUT_PAGE) &&
                (pageConnector != UriPageConnector.PRODUCTS) &&
                (pageConnector != UriPageConnector.PRODUCTS_FILTER) &&
                (pageConnector != UriPageConnector.HOME) &&
                (pageConnector != UriPageConnector.SLASH);
    }

    private void addressRequest(Host host) {
        if (isAuthorized(host)) {
            passToRequiredPage(host);
        } else {
            redirectToLogIn(host);
        }
    }

    private boolean isAuthorized(Host host) {
        Cookie cookieInRequest = servletRequestParser.getTokenCookie(host.parsedRequest);
        return tokenRepository.isPresent(cookieInRequest.getValue());
    }

    private void passToRequiredPage(Host host) {
        try {
            host.chain.doFilter(host.request, host.response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void redirectToLogIn(Host host) {
        try {
            ((HttpServletResponse) host.response).sendRedirect("/login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void redirectToHtmlSourceElement(Host host) {
        try {
            host.chain.doFilter(host.request, host.response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private record Host(Map<String, Object> parsedRequest,
                        ServletRequest request,
                        ServletResponse response,
                        FilterChain chain) {
    }
}
