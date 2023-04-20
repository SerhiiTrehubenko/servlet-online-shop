package com.tsa.shop.servlets.interfaces;

import jakarta.servlet.http.HttpServletResponse;

import java.io.InputStream;

public interface Response {
    Response createResponse();
    Response setServletResponse(HttpServletResponse response);

    Response setHttpStatus(int statusCode);

    Response setContent(InputStream generatedPage);
    HttpServletResponse getServletResponse();
    int getHttpStatusCode();
    InputStream getContent();
}
