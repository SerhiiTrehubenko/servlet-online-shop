package com.tsa.shop.web.impl;

import com.tsa.shop.domain.HttpStatus;
import com.tsa.shop.domain.WebServerException;
import com.tsa.shop.web.interfaces.Response;
import com.tsa.shop.web.interfaces.ResponseWriter;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;

public class DefaultResponseWriter implements ResponseWriter {
    private final static int EMPTY_INPUT = -1;
    private final static int BEGINNING_OF_BUFFER = 0;
    private final static int DEFAULT_BUFFER_SIZE = 1024;
    private final byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
    private int bytesToReadFromBuffer;

    @Override
    public ResponseWriter createInstance() {
        return new DefaultResponseWriter();
    }

    public void write(Response response) {
        try (InputStream input = response.getContent()) {
            HttpServletResponse servletResponse = setHttpStatus(response);
            while (inputContainsData(input)) {
                writeToServletResponse(servletResponse);
            }
        } catch (IOException e) {
            throw new WebServerException("There was a problem during Reading content", e, HttpStatus.INTERNAL_SERVER_ERROR, this);
        }
    }

    private HttpServletResponse setHttpStatus(Response response) {
        HttpServletResponse servletResponse = response.getServletResponse();
        servletResponse.setStatus(response.getHttpStatusCode());
        return servletResponse;
    }

    private boolean inputContainsData(InputStream input) throws IOException {
        bytesToReadFromBuffer = input.read(buffer);
        return bytesToReadFromBuffer != EMPTY_INPUT;
    }

    private void writeToServletResponse(HttpServletResponse servletResponse) throws IOException {
        servletResponse.getOutputStream().write(buffer, BEGINNING_OF_BUFFER, bytesToReadFromBuffer);
    }
}
