package com.tsa.shop.servlets.interfaces;

import jakarta.servlet.http.HttpServletResponse;

import java.io.InputStream;

public class DefaultResponse implements Response {
    private HttpServletResponse servletResponse;
    private int httpStatusCode;

    private InputStream content;

    @Override
    public Response createResponse() {
        return new DefaultResponse();
    }

    @Override
    public Response setServletResponse(HttpServletResponse response) {
        this.servletResponse = response;
        return this;
    }

    @Override
    public Response setHttpStatus(int statusCode) {
        this.httpStatusCode = statusCode;
        return this;
    }

    @Override
    public Response setContent(InputStream content) {
        this.content = content;
        return this;
    }

    public HttpServletResponse getServletResponse() {
        return servletResponse;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public InputStream getContent() {
        return content;
    }

}
