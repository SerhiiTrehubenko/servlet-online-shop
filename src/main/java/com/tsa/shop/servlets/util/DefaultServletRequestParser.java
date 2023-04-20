package com.tsa.shop.servlets.util;

import com.tsa.shop.servlets.enums.UriPageConnector;
import com.tsa.shop.servlets.interfaces.ServletRequestParser;
import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

public class DefaultServletRequestParser implements ServletRequestParser {

    private static final String URI = "URI";
    private static final String PARAMETERS = "parameters";
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

        return parsedRequest;
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
}
