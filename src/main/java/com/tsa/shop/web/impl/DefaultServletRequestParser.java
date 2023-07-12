package com.tsa.shop.web.impl;

import com.tsa.shop.domain.UriPageConnector;
import com.tsa.shop.web.interfaces.ServletRequestParser;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DefaultServletRequestParser implements ServletRequestParser {
    private final static Cookie NULL_COOKIE = new Cookie("null", "null");
    private static final String URI = "URI";
    private static final String PARAMETERS = "parameters";
    private static final String TOKEN_COOKIE = "token-cookie";

    private final UriCache cache;

    public DefaultServletRequestParser(UriCache cache) {
        this.cache = cache;
    }

    public Map<String, Object> parseRequest(HttpServletRequest request) {
        Map<String, Object> parsedRequest = new HashMap<>();

        parsedRequest.put("method", request.getMethod());
        parsedRequest.put(URI, request.getRequestURI());
        parsedRequest.put("URL", String.valueOf(request.getRequestURL()));
        parsedRequest.put("pathInfo", request.getPathInfo());
        parsedRequest.put(PARAMETERS, request.getParameterMap());
        parsedRequest.put(TOKEN_COOKIE, getCookie(request));

        return parsedRequest;
    }

    private Object getCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (Objects.isNull(cookies)) {
            return NULL_COOKIE;
        }
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("user-token"))
                .findFirst().orElse(NULL_COOKIE);
    }

    public Long getIdFromRequest(Map<String, Object> parsedRequest) {
        Map<String, String[]> parameters = getParameters(parsedRequest);

        return Long.parseLong(parameters.get("id")[0]);
    }

    public Map<String, String[]> getParameters(Map<String, Object> parsedRequest) {
        @SuppressWarnings("unchecked")
        Map<String, String[]> parameters = (Map<String, String[]>) parsedRequest.get(PARAMETERS);
        return parameters;
    }

    public String getUri(Map<String, Object> parsedRequest) {
        return String.valueOf(parsedRequest.get(URI));
    }

    public UriPageConnector getUriPageConnector(Map<String, Object> parsedRequest) {
        String uri = getUri(parsedRequest);

        return cache.getUriPageConnector(uri);
    }

    @Override
    public Cookie getTokenCookie(Map<String, Object> parsedRequest) {
        return (Cookie) parsedRequest.get(TOKEN_COOKIE);
    }
}
