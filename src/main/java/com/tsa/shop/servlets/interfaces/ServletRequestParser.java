package com.tsa.shop.servlets.interfaces;

import com.tsa.shop.domain.UriPageConnector;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface ServletRequestParser {
    Map<String, Object> parseRequest(HttpServletRequest request);
    Long getIdFromRequest(Map<String, Object> parsedRequest);
    Map<String, String[]> getParameters(Map<String, Object> parsedRequest);
    String getUri(Map<String, Object> parsedRequest);
    UriPageConnector getUriPageConnector(Map<String, Object> parsedRequest);
    Cookie getTokenCookie(Map<String, Object> parsedRequest);
}
