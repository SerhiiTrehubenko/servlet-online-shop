package com.tsa.shop.servlets.util;

import com.tsa.shop.servlets.enums.UriPageConnector;
import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

public class RequestParser {
    public static Map<String, Object> parseRequest(HttpServletRequest request) {
        Map<String, Object> parsedRequest = new HashMap<>();

        parsedRequest.put("method", request.getMethod());
        parsedRequest.put("URI", request.getRequestURI());
        parsedRequest.put("URL", String.valueOf(request.getRequestURL()));
        parsedRequest.put("pathInfo", request.getPathInfo());
        parsedRequest.put("parameters", request.getParameterMap());

        return parsedRequest;
    }

    public static Long getId(Map<String, Object> parsedRequest) {

        Map<String, String[]> parameters = getParameters(parsedRequest);

        return Long.parseLong(parameters.get("id")[0]);
    }

    public static Map<String, String[]> getParameters(Map<String, Object> parsedRequest) {
        @SuppressWarnings("unchecked")
        Map<String, String[]> parameters = (Map<String, String[]>) parsedRequest.get("parameters");
        return parameters;
    }

    public static UriPageConnector getUriConnector(Map<String, Object> parsedRequest, Map<String, UriPageConnector> mapOfCachedUri) {
        String uriFromParsedRequest = String.valueOf(parsedRequest.get("URI"));
//        var mapOfCachedUri = ServletStarter.CASHED_URI;
        return mapOfCachedUri.get(uriFromParsedRequest);
    }
}
